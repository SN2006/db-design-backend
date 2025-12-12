package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.CourseDTO;
import com.example.app.dbdesignbackend.dto.CreateCourseDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.dto.TeacherDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.dto.UpdateCourseDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CourseDAO {

    private static final String GET_ALL_COURSES = """
            SELECT
                course_id,
                course_name,
                course_description,
                course_level,
                course_duration
            FROM course;
            """;
    private static final String GET_COURSE_BY_NAME = """
            SELECT
                c.course_id,
                c.course_name,
                c.course_description,
                c.course_level,
                c.course_duration,
                t.topic_id,
                t.topic_name
            FROM course c
            LEFT JOIN course_topic ct
            ON c.course_id = ct.course_id
            LEFT JOIN topic t
            ON ct.topic_id = t.topic_id
            WHERE c.course_name=?;
            """;
    private static final String GET_AVAILABLE_GROUPS_FOR_COURSE = """
            SELECT
                group_id,
                course_name,
                start_date
            FROM get_available_group_by_course(?, ?);
            """;
    private static final String GET_TEACHERS_BY_COURSE = """
            SELECT
                t.teacher_id,
                t.teacher_firstname,
                t.teacher_lastname,
                t.teacher_email,
                t.teacher_birthday
            FROM teacher t
            JOIN teacher_course tc
            ON t.teacher_id = tc.teacher_id
            WHERE tc.course_id=?
            """;
    private static final String ADD_COURSE = """
            SELECT add_course_info(?, ?, ?::difficulty_level, ?::interval);
            """;
    private static final String UPDATE_COURSE = """
            SELECT edit_course_info_by_name(?, ?, ?::difficulty_level, ?::interval);
            """;
    private static final String DELETE_COURSE = """
            SELECT delete_course_info_by_name(?);
    """;
    private static final String ADD_TOPIC_TO_COURSE = """
            SELECT add_topic_to_course(?, ?);
            """;
    private static final String DELETE_TOPIC_FROM_COURSE = """
            SELECT delete_topic_from_course(?, ?);
            """;
    private static final String ADD_TEACHER_TO_COURSE = """
            SELECT add_teacher_to_course(?, ?);
            """;
    private static final String DELETE_TEACHER_TO_COURSE = """
            SELECT delete_teacher_from_course(?, ?);
            """;

    private DBConnectionHolder connectionHolder;

    public List<CourseDTO> getCourses() {
        Connection connection = connectionHolder.getConnection();

        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(GET_ALL_COURSES);
            List<CourseDTO> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(CourseDTO.builder()
                        .id(resultSet.getInt("course_id"))
                        .name(resultSet.getString("course_name"))
                        .description(resultSet.getString("course_description"))
                        .level(resultSet.getString("course_level"))
                        .duration(resultSet.getString("course_duration"))
                        .build());
            }
            return courses;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courses", e);
        }
    }

    public Optional<CourseDTO> getCourseByName(String courseName) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COURSE_BY_NAME)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            CourseDTO course = null;
            while (resultSet.next()) {
                if (course == null) {
                    course = CourseDTO.builder()
                            .id(resultSet.getInt("course_id"))
                            .name(resultSet.getString("course_name"))
                            .description(resultSet.getString("course_description"))
                            .level(resultSet.getString("course_level"))
                            .duration(resultSet.getString("course_duration"))
                            .topics(new ArrayList<>())
                            .build();
                }
                if (resultSet.getString("topic_name") == null) {
                    continue;
                }
                course.getTopics().add(
                        TopicDTO.builder()
                                .id(resultSet.getInt("topic_id"))
                                .name(resultSet.getString("topic_name"))
                                .build()
                );
            }
            return Optional.ofNullable(course);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching course by name", e);
        }
    }

    public List<GroupDTO> getAvailableGroupsForCourse(String courseName, int studentId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_GROUPS_FOR_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<GroupDTO> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(
                        GroupDTO.builder()
                                .id(resultSet.getInt("group_id"))
                                .course(
                                        CourseDTO.builder()
                                                .name(resultSet.getString("course_name"))
                                                .build()
                                )
                                .startDate(resultSet.getDate("start_date").toLocalDate())
                                .build()
                );
            }
            return groups;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching available groups for course", e);
        }
    }

    public List<TeacherDTO> getTeachersByCourse(Integer courseId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TEACHERS_BY_COURSE)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<TeacherDTO> teachers = new ArrayList<>();
            while (resultSet.next()) {
                teachers.add(
                        TeacherDTO.builder()
                                .id(resultSet.getInt("teacher_id"))
                                .firstName(resultSet.getString("teacher_firstname"))
                                .lastName(resultSet.getString("teacher_lastname"))
                                .email(resultSet.getString("teacher_email"))
                                .birthday(resultSet.getDate("teacher_birthday").toLocalDate())
                                .build()
                );
            }
            return teachers;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching teachers by course", e);
        }
    }

    public void createCourse(CreateCourseDTO courseDTO) {
        Connection connection = connectionHolder.getConnection();
        Period period = Period.parse(courseDTO.getDuration());

        if (period.getYears() == 0 && period.getMonths() == 0 && period.getDays() < 7) {
            throw new BadRequestException("Course duration must be at least 7 days");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_COURSE)) {
            preparedStatement.setString(1, courseDTO.getName());
            preparedStatement.setString(2, courseDTO.getDescription());
            preparedStatement.setString(3, courseDTO.getLevel());
            preparedStatement.setString(4, courseDTO.getDuration());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void addTopicToCourse(String courseName, String topicName) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TOPIC_TO_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, topicName);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void deleteTopicFromCourse(String courseName, String topicName) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TOPIC_FROM_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, topicName);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void addTeacherToCourse(String courseName, int teacherId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TEACHER_TO_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setInt(2, teacherId);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void deleteTeacherFromCourse(String courseName, int teacherId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEACHER_TO_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setInt(2, teacherId);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void updateCourse(String courseName, UpdateCourseDTO courseDTO) {
        Connection connection = connectionHolder.getConnection();
        Period period = Period.parse(courseDTO.getDuration());

        if (period.getYears() == 0 && period.getMonths() == 0 && period.getDays() < 7) {
            throw new BadRequestException("Course duration must be at least 7 days");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, courseDTO.getDescription());
            preparedStatement.setString(3, courseDTO.getLevel());
            preparedStatement.setString(4, courseDTO.getDuration());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void deleteCourse(String courseName) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COURSE)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
