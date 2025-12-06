package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CreateTopicDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TopicDAO {

    private static final String GET_ALL_TOPIC = """
            SELECT
                t.topic_id,
                t.topic_name
            FROM topic t;
            """;
    private static final String GET_TOPIC_NOT_RELATED_TO_COURSE = """
            SELECT
                t.topic_id,
                t.topic_name
            FROM topic t
            WHERE NOT EXISTS (
                SELECT 1
                FROM course_topic ct
                WHERE ct.topic_id=t.topic_id AND ct.course_id=?
            );
            """;
    private static final String GET_TOPIC_BY_ID = """
            SELECT
                t.topic_id,
                t.topic_name
            FROM topic t
            WHERE t.topic_id=?;
            """;
    private static final String CREATE_TOPIC = """
            INSERT INTO topic (topic_name)
            VALUES (?);
            """;
    private static final String UPDATE_TOPIC = """
            UPDATE topic
            SET topic_name=?
            WHERE topic_id=?;
            """;
    private static final String DELETE_TOPIC = """
            DELETE FROM topic t
            WHERE t.topic_id=?;
            """;

    private DBConnectionHolder connectionHolder;

    public List<TopicDTO> getAllTopics() {
        Connection connection = connectionHolder.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(GET_ALL_TOPIC);
            ResultSet resultSet = statement.getResultSet();

            List<TopicDTO> topics = new ArrayList<>();
            while (resultSet.next()) {
                TopicDTO topic = new TopicDTO(
                        resultSet.getInt("topic_id"),
                        resultSet.getString("topic_name")
                );
                topics.add(topic);
            }
            return topics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TopicDTO> getTopicsNotRelatedToCourse(int courseId) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(GET_TOPIC_NOT_RELATED_TO_COURSE)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            List<TopicDTO> topics = new ArrayList<>();
            while (resultSet.next()) {
                TopicDTO topic = new TopicDTO(
                        resultSet.getInt("topic_id"),
                        resultSet.getString("topic_name")
                );
                topics.add(topic);
            }
            return topics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TopicDTO> getTopicById(int topicId) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(GET_TOPIC_BY_ID)) {
            preparedStatement.setInt(1, topicId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                TopicDTO topic = new TopicDTO(
                        resultSet.getInt("topic_id"),
                        resultSet.getString("topic_name")
                );
                return Optional.of(topic);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTopic(CreateTopicDTO createTopicDTO) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(CREATE_TOPIC)) {
            preparedStatement.setString(1, createTopicDTO.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void updateTopic(int topicId, CreateTopicDTO updateTopicDTO) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(UPDATE_TOPIC)) {
            preparedStatement.setString(1, updateTopicDTO.getName());
            preparedStatement.setInt(2, topicId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void deleteTopic(int topicId) {
        Connection connection = connectionHolder.getConnection();

        try (var preparedStatement = connection.prepareStatement(DELETE_TOPIC)) {
            preparedStatement.setInt(1, topicId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
