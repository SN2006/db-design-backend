package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.Achievement;
import com.example.app.dbdesignbackend.dto.StudentDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class StudentDAO {

    private static final String GET_STUDENT_BY_ID = """
            SELECT
                student_id,
                student_firstname,
                student_lastname,
                student_email,
                student_birthday
            FROM student
            WHERE student_id=?;
            """;
    private static final String GET_ACHIEVEMENTS_FOR_STUDENT = """
            SELECT
                g.group_id,
                c.course_name,
                (
                    SELECT
                        avg_grade
                    FROM get_avg_grade(?, sg.group_id)
                    )
            FROM student_group sg
            JOIN group_ g
            ON sg.group_id = g.group_id
            JOIN course c
            ON g.course_id = c.course_id
            WHERE sg.student_id=?;
            """;

    private DBConnectionHolder connectionHolder;

    public Optional<StudentDTO> getStudentById(Integer studentId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENT_BY_ID)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                StudentDTO studentDTO = StudentDTO.builder()
                        .id(resultSet.getInt("student_id"))
                        .firstName(resultSet.getString("student_firstname"))
                        .lastName(resultSet.getString("student_lastname"))
                        .email(resultSet.getString("student_email"))
                        .birthday(resultSet.getDate("student_birthday").toLocalDate())
                        .build();
                return Optional.of(studentDTO);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<Achievement> getAchievementsForStudent(Integer studentId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ACHIEVEMENTS_FOR_STUDENT)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Achievement> achievements = new java.util.ArrayList<>();
            while (resultSet.next()) {
                Achievement achievement = Achievement.builder()
                        .groupId(resultSet.getInt("group_id"))
                        .courseName(resultSet.getString("course_name"))
                        .averageGrade(resultSet.getDouble("avg_grade"))
                        .build();
                achievements.add(achievement);
            }
            return achievements;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
