package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.HomeworkSolutionDAO;
import com.example.app.dbdesignbackend.dto.EvaluateHomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.HomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.SendHomeworkSolutionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeworkSolutionService {

    private HomeworkSolutionDAO homeworkSolutionDAO;

    public List<HomeworkSolutionDTO> findAllByHomeworkId(int homeworkId) {
        return homeworkSolutionDAO.getAllHomeworkSolutionByHomeworkId(homeworkId);
    }

    public List<HomeworkSolutionDTO> findAllByHomeworkAndStudent(
            int homeworkId,
            int studentId
    ) {
        return homeworkSolutionDAO.getHomeworkSolutionsByHomeworkAndStudent(homeworkId, studentId);
    }

    public void sendSolution(
            int studentId,
            SendHomeworkSolutionDTO solution
    ) {
        homeworkSolutionDAO.sendHomeworkSolution(studentId, solution);
    }

    public void evaluateHomeworkSolution(
            int homeworkSolutionId,
            EvaluateHomeworkSolutionDTO evaluateHomeworkSolutionDTO
    ) {
        homeworkSolutionDAO.evaluateHomeworkSolution(homeworkSolutionId, evaluateHomeworkSolutionDTO);
    }

}
