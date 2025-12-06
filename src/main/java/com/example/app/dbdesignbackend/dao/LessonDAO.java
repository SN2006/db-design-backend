package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.LessonDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

}
