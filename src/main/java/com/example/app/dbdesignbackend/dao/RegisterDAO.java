package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionFactory;
import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.RegistrationDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import com.example.app.dbdesignbackend.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class RegisterDAO {

    private final static String REGISTER_QUERY = """
            SELECT register_user(?, ?, ?, ?, ?, ?);
            """;

    private DBConnectionFactory dbConnectionFactory;

    public void register(RegistrationDTO registrationDTO, Role role) {
        Connection connection = dbConnectionFactory.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_QUERY)) {
            preparedStatement.setString(1, registrationDTO.getFirstName());
            preparedStatement.setString(2, registrationDTO.getLastName());
            preparedStatement.setString(3, registrationDTO.getEmail());
            preparedStatement.setString(4, registrationDTO.getPassword());
            preparedStatement.setDate(5, new java.sql.Date(registrationDTO.getBirthday().getTime()));
            preparedStatement.setString(6, role.getAuthority().toLowerCase());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException("Email is already in use.");
        }
    }

}
