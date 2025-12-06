package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CreateScheduleDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class ScheduleDAO {

    private static final String CREATE_SCHEDULE = """
            SELECT create_schedule(?, ?, ?, ?);
            """;

    private DBConnectionHolder connectionHolder;

    public void createSchedule(CreateScheduleDTO createScheduleDTO) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SCHEDULE)) {
            preparedStatement.setInt(1, createScheduleDTO.getGroupId());
            preparedStatement.setInt(2, createScheduleDTO.getTeacherId());
            preparedStatement.setInt(3, createScheduleDTO.getLessonId());
            preparedStatement.setObject(4, createScheduleDTO.getScheduleDate());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
