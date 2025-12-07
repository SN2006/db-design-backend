package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.LessonDAO;
import com.example.app.dbdesignbackend.dto.CreateLessonDTO;
import com.example.app.dbdesignbackend.dto.LessonDTO;
import com.example.app.dbdesignbackend.dto.UpdateLessonDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {

    private LessonDAO lessonDAO;

    public List<LessonDTO> findAll() {
        return lessonDAO.getAllLessons();
    }

    public void createLesson(CreateLessonDTO createLessonDTO) {
        lessonDAO.createLesson(createLessonDTO);
    }

    public void updateLessonByName(String name, UpdateLessonDTO updateLessonDTO) {
        lessonDAO.updateLessonByName(name, updateLessonDTO);
    }

}
