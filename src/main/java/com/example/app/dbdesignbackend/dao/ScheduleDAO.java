package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CourseDTO;
import com.example.app.dbdesignbackend.dto.CreateScheduleDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.dto.LessonDTO;
import com.example.app.dbdesignbackend.dto.ScheduleDTO;
import com.example.app.dbdesignbackend.dto.TeacherDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ScheduleDAO {

    private static final String GET_ALL_SCHEDULES = """
            SELECT
                s.schedule_id,
                s.schedule_date,
                l.lesson_id,
                l.lesson_name,
                l.lesson_description,
                l.lesson_duration,
                top.topic_id,
                top.topic_name,
                g.group_id,
                g.group_start_date,
                g.course_id,
                g.is_finished,
                t.teacher_id,
                t.teacher_firstname,
                t.teacher_lastname,
                t.teacher_email,
                t.teacher_birthday
            FROM schedule s
            JOIN lesson l
            ON s.lesson_id = l.lesson_id
            JOIN group_ g
            ON s.group_id = g.group_id
            JOIN teacher t
            ON s.teacher_id = t.teacher_id
            JOIN topic top
            ON l.topic_id = top.topic_id;
            """;
    private static final String GET_ALL_SCHEDULES_FOR_GROUP = """
            SELECT
                lesson_id,
                lesson_name,
                lesson_description,
                schedule_date,
                teacher_id,
                teacher_firstname,
                teacher_lastname
            FROM get_schedule_by_group(?);
            """;
    private static final String CREATE_SCHEDULE = """
            SELECT create_schedule(?, ?, ?, ?);
            """;

    private DBConnectionHolder connectionHolder;

    public List<ScheduleDTO> getAllSchedules() {
        Connection connection = connectionHolder.getConnection();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_SCHEDULES);

            List<ScheduleDTO> schedules = new ArrayList<>();

            while (resultSet.next()) {
                schedules.add(
                        ScheduleDTO.builder()
                                .id(resultSet.getInt("schedule_id"))
                                .dateTime(resultSet.getTimestamp("schedule_date").toLocalDateTime())
                                .lesson(
                                        LessonDTO.builder()
                                                .id(resultSet.getInt("lesson_id"))
                                                .name(resultSet.getString("lesson_name"))
                                                .description(resultSet.getString("lesson_description"))
                                                .duration(resultSet.getString("lesson_duration"))
                                                .topic(
                                                        TopicDTO.builder()
                                                                .id(resultSet.getInt("topic_id"))
                                                                .name(resultSet.getString("topic_name"))
                                                                .build()
                                                )
                                                .build()
                                )
                                .group(
                                        GroupDTO.builder()
                                                .id(resultSet.getInt("group_id"))
                                                .startDate(resultSet.getDate("group_start_date").toLocalDate())
                                                .course(
                                                        CourseDTO.builder()
                                                                .id(resultSet.getInt("course_id"))
                                                                .build()
                                                )
                                                .isFinished(resultSet.getBoolean("is_finished"))
                                                .build()
                                )
                                .teacher(
                                        TeacherDTO.builder()
                                                .id(resultSet.getInt("teacher_id"))
                                                .firstName(resultSet.getString("teacher_firstname"))
                                                .lastName(resultSet.getString("teacher_lastname"))
                                                .email(resultSet.getString("teacher_email"))
                                                .birthday(resultSet.getDate("teacher_birthday").toLocalDate())
                                                .build()
                                )
                                .build()
                );
            }

            return schedules;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<ScheduleDTO> getAllSchedulesForGroup(Integer groupId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SCHEDULES_FOR_GROUP)) {
            preparedStatement.setInt(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ScheduleDTO> schedules = new ArrayList<>();

            while (resultSet.next()) {
                schedules.add(
                        ScheduleDTO.builder()
                                .dateTime(resultSet.getTimestamp("schedule_date").toLocalDateTime())
                                .lesson(
                                        LessonDTO.builder()
                                                .id(resultSet.getInt("lesson_id"))
                                                .name(resultSet.getString("lesson_name"))
                                                .description(resultSet.getString("lesson_description"))
                                                .build()
                                )
                                .teacher(
                                        TeacherDTO.builder()
                                                .id(resultSet.getInt("teacher_id"))
                                                .firstName(resultSet.getString("teacher_firstname"))
                                                .lastName(resultSet.getString("teacher_lastname"))
                                                .build()
                                )
                                .build()
                );
            }

            return schedules;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

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
