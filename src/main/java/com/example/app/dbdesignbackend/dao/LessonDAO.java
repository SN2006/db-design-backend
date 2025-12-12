package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CreateLessonDTO;
import com.example.app.dbdesignbackend.dto.LessonDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.dto.UpdateLessonDTO;
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
public class LessonDAO {

    private static final String GET_ALL_LESSONS = """
            SELECT
                l.lesson_id,
                l.lesson_name,
                l.lesson_description,
                l.lesson_duration,
                t.topic_id,
                t.topic_name
            FROM lesson l
            JOIN topic t
            ON l.topic_id = t.topic_id;
            """;
    private static final String GET_LESSON_BY_NAME = """
            SELECT
                l.lesson_id,
                l.lesson_name,
                l.lesson_description,
                l.lesson_duration,
                t.topic_id,
                t.topic_name
            FROM lesson l
            JOIN topic t
            ON l.topic_id = t.topic_id
            WHERE l.lesson_name = ?;
            """;
    private static final String CREATE_LESSON = """
            SELECT create_lesson(?, ?, ?, ?::interval);
            """;
    private static final String UPDATE_LESSON_BY_NAME = """
            SELECT update_lesson_by_name(?, ?, ?, ?::interval);
            """;

    private DBConnectionHolder dbConnectionHolder;

    public List<LessonDTO> getAllLessons() {
        Connection connection = dbConnectionHolder.getConnection();

        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(GET_ALL_LESSONS);
            List<LessonDTO> lessons = new ArrayList<>();

            while (resultSet.next()) {
                LessonDTO lesson = LessonDTO.builder()
                        .id(resultSet.getInt("lesson_id"))
                        .name(resultSet.getString("lesson_name"))
                        .description(resultSet.getString("lesson_description"))
                        .duration(resultSet.getString("lesson_duration"))
                        .topic(new TopicDTO(
                                resultSet.getInt("topic_id"),
                                resultSet.getString("topic_name")
                        ))
                        .build();
                lessons.add(lesson);
            }
            return lessons;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving lessons", e);
        }
    }

    public Optional<LessonDTO> getLessonByName(String name) {
        Connection connection = dbConnectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(GET_LESSON_BY_NAME)) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                LessonDTO lesson = LessonDTO.builder()
                        .id(resultSet.getInt("lesson_id"))
                        .name(resultSet.getString("lesson_name"))
                        .description(resultSet.getString("lesson_description"))
                        .duration(resultSet.getString("lesson_duration"))
                        .topic(TopicDTO.builder()
                                .id(resultSet.getInt("topic_id"))
                                .name(resultSet.getString("topic_name"))
                                .build())
                        .build();
                return Optional.of(lesson);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving lesson by name", e);
        }
    }

    public void createLesson(CreateLessonDTO createLessonDTO) {
        Connection connection = dbConnectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(CREATE_LESSON)) {
            preparedStatement.setString(1, createLessonDTO.getLessonName());
            preparedStatement.setString(2, createLessonDTO.getTopicName());
            preparedStatement.setString(3, createLessonDTO.getDescription());
            preparedStatement.setString(4, createLessonDTO.getDuration());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void updateLessonByName(String name, UpdateLessonDTO updateLessonDTO) {
        Connection connection = dbConnectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(UPDATE_LESSON_BY_NAME)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, updateLessonDTO.getTopicName());
            preparedStatement.setString(3, updateLessonDTO.getDescription());
            preparedStatement.setString(4, updateLessonDTO.getDuration());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
