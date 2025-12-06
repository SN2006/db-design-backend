package com.example.app.dbdesignbackend.filter;

import com.example.app.dbdesignbackend.db.DBConnectionFactory;
import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CredentialDTO;
import com.example.app.dbdesignbackend.exception.UnableConnectToDatabaseException;
import com.example.app.dbdesignbackend.security.Role;
import com.example.app.dbdesignbackend.security.User;
import com.example.app.dbdesignbackend.util.BasicAuthTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class DatabaseAuthFilter extends OncePerRequestFilter {

    private final static String GET_USER_BY_EMAIL = """
    SELECT
        user_id,
        firstname,
        lastname,
        email,
        role
    FROM get_user_by_email(?);
""";

    private DBConnectionFactory dbConnectionFactory;
    private DBConnectionHolder dbConnectionHolder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        CredentialDTO credentialDTO = null;

        try {
            credentialDTO = BasicAuthTokenUtil.parseToken(authHeader);
        } catch (IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        }

        try (Connection connection = dbConnectionFactory.getConnection(
                credentialDTO.getEmail(), credentialDTO.getPassword()
        )) {
            dbConnectionHolder.setConnection(connection);

            User user = getUser(connection, credentialDTO.getEmail());

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.singletonList(user.getRole())
                    )
            );

            filterChain.doFilter(request, response);
        } catch (UnableConnectToDatabaseException | SQLException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid database credentials");
        }
    }

    private User getUser(Connection connection, String email) {
        try (var preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return User.builder()
                        .id(resultSet.getInt("user_id"))
                        .firstName(resultSet.getString("firstname"))
                        .lastName(resultSet.getString("lastname"))
                        .email(resultSet.getString("email"))
                        .role(Role.fromString(resultSet.getString("role")))
                        .build();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user details", e);
        }
    }
}
