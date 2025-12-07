package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.HomeworkDAO;
import com.example.app.dbdesignbackend.dto.CreateHomeworkDTO;
import com.example.app.dbdesignbackend.dto.HomeworkDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeworkService {

    private HomeworkDAO homeworkDAO;

    public List<HomeworkDTO> findAllByGroupId(Integer groupId) {
        return homeworkDAO.getHomeworksByGroupId(groupId);
    }

    public void createHomework(CreateHomeworkDTO createHomeworkDTO) {
        homeworkDAO.createHomework(createHomeworkDTO);
    }

    public void updateHomework(int homeworkId, CreateHomeworkDTO createHomeworkDTO) {
        homeworkDAO.updateHomework(homeworkId, createHomeworkDTO);
    }

    public void deleteHomework(int homeworkId) {
        homeworkDAO.deleteHomework(homeworkId);
    }

}
