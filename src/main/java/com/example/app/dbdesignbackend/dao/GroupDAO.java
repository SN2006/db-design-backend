package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CourseDTO;
import com.example.app.dbdesignbackend.dto.CreateGroupDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GroupDAO {

    private static final String GET_ALL_GROUPS = """
            SELECT
                g.group_id,
                g.group_start_date,
                g.course_id,
                g.is_finished,
                c.course_id,
                c.course_name,
                c.course_description,
                c.course_level,
                c.course_duration
            FROM group_ g
            JOIN course c
            ON g.course_id = c.course_id;
            """;
    private static final String GET_ALL_GROUPS_FOR_TEACHER = """
            SELECT
                DISTINCT g.group_id,
                g.group_start_date,
                g.is_finished,
                c.course_id,
                c.course_name,
                c.course_description,
                c.course_level,
                c.course_duration
            FROM schedule s
            JOIN group_ g
            ON s.group_id = g.group_id
            JOIN course c
            ON g.course_id = c.course_id
            WHERE s.teacher_id=?;
            """;
    private static final String GET_ALL_GROUPS_FOR_STUDENT = """
            SELECT
                DISTINCT g.group_id,
                g.group_start_date,
                g.is_finished,
                c.course_id,
                c.course_name,
                c.course_description,
                c.course_level,
                c.course_duration
            FROM student_group sg
            JOIN group_ g
            ON sg.group_id = g.group_id
            JOIN course c
            ON g.course_id = c.course_id
            WHERE sg.student_id=?;
            """;
    private static final String GET_AVAILABLE_GROUPS_FOR_STUDENT = """
            SELECT
                course_name,
                course_description,
                course_level,
                start_date,
                group_id
            FROM get_available_course(?);
            """;
    private static final String CREATE_GROUP = """
            SELECT create_group(?, ?);
            """;
    private static final String JOIN_GROUP = """
            SELECT join_group(?, ?);
            """;

    private DBConnectionHolder connectionHolder;

    public List<GroupDTO> getAllGroups() {
        Connection connection = connectionHolder.getConnection();

        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(GET_ALL_GROUPS);

            List<GroupDTO> groups = new ArrayList<>();

            while (resultSet.next()) {
                groups.add(GroupDTO.builder()
                        .id(resultSet.getInt("group_id"))
                        .startDate(resultSet.getDate("group_start_date").toLocalDate())
                        .isFinished(resultSet.getBoolean("is_finished"))
                        .course(
                                CourseDTO.builder()
                                        .id(resultSet.getInt("course_id"))
                                        .name(resultSet.getString("course_name"))
                                        .description(resultSet.getString("course_description"))
                                        .level(resultSet.getString("course_level"))
                                        .duration(resultSet.getString("course_duration"))
                                        .build()
                        )
                        .build());
            }

            return groups;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<GroupDTO> getAllGroupsForTeacher(int teacherId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_GROUPS_FOR_TEACHER)) {
            ps.setInt(1, teacherId);
            var resultSet = ps.executeQuery();

            List<GroupDTO> groups = new ArrayList<>();

            while (resultSet.next()) {
                groups.add(GroupDTO.builder()
                        .id(resultSet.getInt("group_id"))
                        .startDate(resultSet.getDate("group_start_date").toLocalDate())
                        .isFinished(resultSet.getBoolean("is_finished"))
                        .course(
                                CourseDTO.builder()
                                        .id(resultSet.getInt("course_id"))
                                        .name(resultSet.getString("course_name"))
                                        .description(resultSet.getString("course_description"))
                                        .level(resultSet.getString("course_level"))
                                        .duration(resultSet.getString("course_duration"))
                                        .build()
                        )
                        .build());
            }

            return groups;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<GroupDTO> getAllGroupsForStudent(int studentId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_GROUPS_FOR_STUDENT)) {
            ps.setInt(1, studentId);
            var resultSet = ps.executeQuery();

            List<GroupDTO> groups = new ArrayList<>();

            while (resultSet.next()) {
                groups.add(GroupDTO.builder()
                        .id(resultSet.getInt("group_id"))
                        .startDate(resultSet.getDate("group_start_date").toLocalDate())
                        .isFinished(resultSet.getBoolean("is_finished"))
                        .course(
                                CourseDTO.builder()
                                        .id(resultSet.getInt("course_id"))
                                        .name(resultSet.getString("course_name"))
                                        .description(resultSet.getString("course_description"))
                                        .level(resultSet.getString("course_level"))
                                        .duration(resultSet.getString("course_duration"))
                                        .build()
                        )
                        .build());
            }

            return groups;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<GroupDTO> getAvailableGroupsForStudent(int studentId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement ps = connection.prepareStatement(GET_AVAILABLE_GROUPS_FOR_STUDENT)) {
            ps.setInt(1, studentId);
            var resultSet = ps.executeQuery();

            List<GroupDTO> groups = new ArrayList<>();

            while (resultSet.next()) {
                groups.add(GroupDTO.builder()
                        .id(resultSet.getInt("group_id"))
                        .startDate(resultSet.getDate("start_date").toLocalDate())
                        .course(
                                CourseDTO.builder()
                                        .name(resultSet.getString("course_name"))
                                        .description(resultSet.getString("course_description"))
                                        .level(resultSet.getString("course_level"))
                                        .build()
                        )
                        .build());
            }

            return groups;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void createGroup(CreateGroupDTO createGroupDTO) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(CREATE_GROUP)) {
            ps.setString(1, createGroupDTO.getCourseName());
            ps.setDate(2, Date.valueOf(createGroupDTO.getStartDate()));
            ps.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void joinGroup(int studentId, int groupId) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(JOIN_GROUP)) {
            ps.setInt(1, studentId);
            ps.setInt(2, groupId);
            ps.execute();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
