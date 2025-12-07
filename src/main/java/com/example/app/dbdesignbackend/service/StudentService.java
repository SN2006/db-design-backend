package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.StudentDAO;
import com.example.app.dbdesignbackend.dto.StudentDTO;
import com.example.app.dbdesignbackend.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentDAO studentDAO;

    public StudentDTO findStudentById(Integer studentId) {
        Optional<StudentDTO> studentDTOOptional = studentDAO.getStudentById(studentId);

        if (studentDTOOptional.isEmpty()) {
            throw new EntityNotFoundException("Student with id " + studentId + " not found");
        }

        StudentDTO studentDTO = studentDTOOptional.get();
        studentDTO.setAchievements(studentDAO.getAchievementsForStudent(studentId));

        return studentDTO;
    }

}
