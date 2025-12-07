package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.HomeworkSolutionDAO;
import com.example.app.dbdesignbackend.dto.EvaluateHomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.HomeworkSolutionDTO;
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

    public void evaluateHomeworkSolution(
            int homeworkSolutionId,
            EvaluateHomeworkSolutionDTO evaluateHomeworkSolutionDTO
    ) {
        homeworkSolutionDAO.evaluateHomeworkSolution(homeworkSolutionId, evaluateHomeworkSolutionDTO);
    }

}
