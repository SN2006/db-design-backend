package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.TeacherDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TeacherDAO {

    private static final String GET_ALL_TEACHERS = """
            SELECT
                t.teacher_id,
                t.teacher_firstname,
                t.teacher_lastname,
                t.teacher_email,
                t.teacher_birthday
            FROM teacher t;
            """;
    private static final String GET_TEACHERS_NOT_RELATED_TO_COURSE = """
            SELECT
                t.teacher_id,
                t.teacher_firstname,
                t.teacher_lastname,
                t.teacher_email,
                t.teacher_birthday
            FROM teacher t
            WHERE NOT EXISTS(
                SELECT 1
                FROM teacher_course tc
                WHERE tc.teacher_id=t.teacher_id AND tc.course_id=?
            );
            """;
    private static final String GET_TEACHER_BY_ID = """
            SELECT
                t.teacher_id,
                t.teacher_firstname,
                t.teacher_lastname,
                t.teacher_email,
                t.teacher_birthday
            FROM teacher t
            WHERE t.teacher_id = ?;
            """;
    private static final String GET_TOPICS_BY_TEACHER_ID = """
            SELECT
                topic_name
            FROM find_topics_by_teacher_id(?);
            """;

    private DBConnectionHolder connectionHolder;

    public List<TeacherDTO> getAllTeachers() {
        Connection connection = connectionHolder.getConnection();

        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(GET_ALL_TEACHERS);
            List<TeacherDTO> teachers = new ArrayList<>();
            while (resultSet.next()) {
                TeacherDTO teacher = TeacherDTO.builder()
                        .id(resultSet.getInt("teacher_id"))
                        .firstName(resultSet.getString("teacher_firstname"))
                        .lastName(resultSet.getString("teacher_lastname"))
                        .email(resultSet.getString("teacher_email"))
                        .birthday(resultSet.getDate("teacher_birthday").toLocalDate())
                        .topics(new ArrayList<>())
                        .build();
                teachers.add(teacher);
            }
            return teachers;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching teachers", e);
        }
    }

    public List<TeacherDTO> getAllTeachersNotRelatedToCourse(int courseId) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(GET_TEACHERS_NOT_RELATED_TO_COURSE)) {
            preparedStatement.setInt(1, courseId);
            var resultSet = preparedStatement.executeQuery();
            List<TeacherDTO> teachers = new ArrayList<>();
            while (resultSet.next()) {
                TeacherDTO teacher = TeacherDTO.builder()
                        .id(resultSet.getInt("teacher_id"))
                        .firstName(resultSet.getString("teacher_firstname"))
                        .lastName(resultSet.getString("teacher_lastname"))
                        .email(resultSet.getString("teacher_email"))
                        .birthday(resultSet.getDate("teacher_birthday").toLocalDate())
                        .topics(new ArrayList<>())
                        .build();
                teachers.add(teacher);
            }
            return teachers;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching teachers not related to course", e);
        }
    }

    public Optional<TeacherDTO> getTeacherById(int teacherId) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(GET_TEACHER_BY_ID)) {
            preparedStatement.setInt(1, teacherId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                TeacherDTO teacher = TeacherDTO.builder()
                        .id(resultSet.getInt("teacher_id"))
                        .firstName(resultSet.getString("teacher_firstname"))
                        .lastName(resultSet.getString("teacher_lastname"))
                        .email(resultSet.getString("teacher_email"))
                        .birthday(resultSet.getDate("teacher_birthday").toLocalDate())
                        .build();
                return Optional.of(teacher);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching teacher by ID", e);
        }
    }

    public List<TopicDTO> getTopicsByTeacherId(int teacherId) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(GET_TOPICS_BY_TEACHER_ID)) {
            preparedStatement.setInt(1, teacherId);
            var resultSet = preparedStatement.executeQuery();
            List<TopicDTO> topics = new ArrayList<>();
            while (resultSet.next()) {
                topics.add(TopicDTO.builder()
                                .name(resultSet.getString("topic_name"))
                        .build());
            }
            return topics;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
