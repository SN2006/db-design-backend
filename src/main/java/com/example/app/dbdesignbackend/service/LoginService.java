package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.db.DBConnectionFactory;
import com.example.app.dbdesignbackend.dto.CredentialDTO;
import com.example.app.dbdesignbackend.dto.LoginResponseDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import com.example.app.dbdesignbackend.util.BasicAuthTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
@AllArgsConstructor
public class LoginService {

    private final static String GET_USER_BY_EMAIL = """
    SELECT
        firstname,
        lastname,
        email,
        role
    FROM get_user_by_email(?);
""";

    private DBConnectionFactory dbConnectionFactory;

    public LoginResponseDTO login(CredentialDTO credentialDTO) {
        try (Connection connection = dbConnectionFactory.getConnection(credentialDTO.getEmail(), credentialDTO.getPassword())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
                preparedStatement.setString(1, credentialDTO.getEmail());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return LoginResponseDTO.builder()
                            .firstName(resultSet.getString("firstname"))
                            .lastName(resultSet.getString("lastname"))
                            .email(resultSet.getString("email"))
                            .role(resultSet.getString("role"))
                            .token(BasicAuthTokenUtil.generateToken(credentialDTO))
                            .build();
                }
            }
            return new LoginResponseDTO();
        } catch (Exception e) {
            throw new BadRequestException("Invalid credentials");
        }
    }

}
