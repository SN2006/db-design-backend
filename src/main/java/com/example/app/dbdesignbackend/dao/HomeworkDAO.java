package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CreateHomeworkDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@AllArgsConstructor
public class HomeworkDAO {

    private static final String CREATE_HOMEWORK = """
            SELECT create_homework(?, ?, ?, ?);
            """;
    private static final String UPDATE_HOMEWORK = """
            SELECT update_homework(?, ?, ?, ?, ?);
            """;
    private static final String DELETE_HOMEWORK_BY_ID = """
            DELETE FROM homework
            WHERE homework_id = ?;
            """;

    private DBConnectionHolder connectionHolder;

    public void createHomework(CreateHomeworkDTO createHomeworkDTO) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_HOMEWORK, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, createHomeworkDTO.getLessonId());
            preparedStatement.setInt(2, createHomeworkDTO.getGroupId());
            preparedStatement.setString(3, createHomeworkDTO.getName());
            preparedStatement.setDate(4, Date.valueOf(createHomeworkDTO.getDeadline()));

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void updateHomework(int homeworkId, CreateHomeworkDTO createHomeworkDTO) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_HOMEWORK, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, homeworkId);
            preparedStatement.setInt(2, createHomeworkDTO.getLessonId());
            preparedStatement.setInt(3, createHomeworkDTO.getGroupId());
            preparedStatement.setString(4, createHomeworkDTO.getName());
            preparedStatement.setDate(5, Date.valueOf(createHomeworkDTO.getDeadline()));

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public  void deleteHomework(int homeworkId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_HOMEWORK_BY_ID)) {
            preparedStatement.setInt(1, homeworkId);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
