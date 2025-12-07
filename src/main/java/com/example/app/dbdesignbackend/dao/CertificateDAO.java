package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CertificateDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class CertificateDAO {

    private static final String GET_CERTIFICATE_BY_STUDENT = """
            SELECT
                certificate_number,
                course_name,
                certificate_date
            FROM get_certificates(?);
            """;

    private DBConnectionHolder connectionHolder;

    public List<CertificateDTO> getCertificatesByStudentId(Integer studentId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_CERTIFICATE_BY_STUDENT)) {
            preparedStatement.setInt(1, studentId);

            var resultSet = preparedStatement.executeQuery();
            List<CertificateDTO> certificates = new java.util.ArrayList<>();

            while (resultSet.next()) {
                CertificateDTO certificateDTO = CertificateDTO.builder()
                        .number(resultSet.getInt("certificate_number"))
                        .courseName(resultSet.getString("course_name"))
                        .date(resultSet.getDate("certificate_date").toLocalDate())
                        .build();
                certificates.add(certificateDTO);
            }

            return certificates;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
