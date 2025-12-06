package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.TeacherDAO;
import com.example.app.dbdesignbackend.dto.TeacherDTO;
import com.example.app.dbdesignbackend.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherService {

    private TeacherDAO teacherDAO;

    public List<TeacherDTO> findAll() {
        return teacherDAO.getAllTeachers();
    }

    public List<TeacherDTO> findAllNotRelatedToCourse(int courseId) {
        return teacherDAO.getAllTeachersNotRelatedToCourse(courseId);
    }

    public TeacherDTO findById(int id) {
        Optional<TeacherDTO> teacherOpt = teacherDAO.getTeacherById(id);
        if (teacherOpt.isEmpty()) {
            throw new EntityNotFoundException("Teacher with id " + id + " not found");
        }

        TeacherDTO teacherDTO = teacherOpt.get();
        teacherDTO.setTopics(teacherDAO.getTopicsByTeacherId(id));
        return teacherDTO;
    }

}
