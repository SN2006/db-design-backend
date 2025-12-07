package com.example.app.dbdesignbackend.dao;

import com.example.app.dbdesignbackend.db.DBConnectionHolder;
import com.example.app.dbdesignbackend.dto.EvaluateHomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.HomeworkDTO;
import com.example.app.dbdesignbackend.dto.HomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.SendHomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.StudentDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class HomeworkSolutionDAO {

    private static final String GET_HOMEWORK_SOLUTION_BY_HOMEWORK_ID = """
            SELECT
                homework_solution_id,
                homework_id,
                homework_name,
                student_id,
                student_firstname,
                student_lastname,
                solution,
                status,
                grade,
                feedback
            FROM get_homework_solutions_by_homework_id(?);
            """;
    private static final String GET_HOMEWORK_SOLUTIONS_BY_HOMEWORK_AND_STUDENT = """
            SELECT
                hs.homework_solution_id,
                hs.homework_solution_grade,
                hs.homework_solution_feedback,
                hs.homework_solution_status,
                hs.homework_solution_solution
            FROM homework_solution hs
            JOIN homework_homework_solution hhs
            ON hs.homework_solution_id = hhs.homework_solution_id
            WHERE hhs.homework_id=?
            AND hs.student_id=?;
            """;
    private static final String SEND_HOMEWORK_SOLUTION = """
            SELECT send_homework_solution(?, ?, ?);
            """;
    private static final String EVALUATE_HOMEWORK_SOLUTION = """
            SELECT evaluate_homework_solution(?, ?, ?);
            """;

    private DBConnectionHolder connectionHolder;

    public List<HomeworkSolutionDTO> getAllHomeworkSolutionByHomeworkId(int homeworkId) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_HOMEWORK_SOLUTION_BY_HOMEWORK_ID)){
            preparedStatement.setInt(1, homeworkId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<HomeworkSolutionDTO> homeworkSolutions = new ArrayList<>();

            while (resultSet.next()) {
                homeworkSolutions.add(
                        HomeworkSolutionDTO.builder()
                                .id(resultSet.getInt("homework_solution_id"))
                                .student(
                                        StudentDTO.builder()
                                                .id(resultSet.getInt("student_id"))
                                                .firstName(resultSet.getString("student_firstname"))
                                                .lastName(resultSet.getString("student_lastname"))
                                                .build()
                                )
                                .solution(resultSet.getString("solution"))
                                .status(resultSet.getString("status"))
                                .grade(resultSet.getInt("grade"))
                                .feedback(resultSet.getString("feedback"))
                                .homework(
                                        HomeworkDTO.builder()
                                                .id(resultSet.getInt("homework_id"))
                                                .name(resultSet.getString("homework_name"))
                                                .build()
                                )
                                .build()
                );
            }

            return homeworkSolutions;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<HomeworkSolutionDTO> getHomeworkSolutionsByHomeworkAndStudent(
            int homeworkId,
            int studentId
    ) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_HOMEWORK_SOLUTIONS_BY_HOMEWORK_AND_STUDENT)){
            preparedStatement.setInt(1, homeworkId);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<HomeworkSolutionDTO> homeworkSolutions = new ArrayList<>();

            while (resultSet.next()) {
                homeworkSolutions.add(
                        HomeworkSolutionDTO.builder()
                                .id(resultSet.getInt("homework_solution_id"))
                                .solution(resultSet.getString("homework_solution_solution"))
                                .status(resultSet.getString("homework_solution_status"))
                                .grade(resultSet.getInt("homework_solution_grade"))
                                .feedback(resultSet.getString("homework_solution_feedback"))
                                .build()
                );
            }

            return homeworkSolutions;
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void sendHomeworkSolution(
            int studentId,
            SendHomeworkSolutionDTO sendHomeworkSolutionDTO
    ) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SEND_HOMEWORK_SOLUTION)){
            preparedStatement.setInt(1, sendHomeworkSolutionDTO.getHomeworkId());
            preparedStatement.setInt(2, studentId);
            preparedStatement.setString(3, sendHomeworkSolutionDTO.getSolution());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void evaluateHomeworkSolution(
            int homeworkSolutionId,
            EvaluateHomeworkSolutionDTO evaluateHomeworkSolutionDTO
    ) {
        Connection connection = connectionHolder.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(EVALUATE_HOMEWORK_SOLUTION)){
            preparedStatement.setInt(1, homeworkSolutionId);
            preparedStatement.setInt(2, evaluateHomeworkSolutionDTO.getGrade());
            preparedStatement.setString(3, evaluateHomeworkSolutionDTO.getFeedback());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
