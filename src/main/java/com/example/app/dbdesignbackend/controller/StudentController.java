package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.StudentDTO;
import com.example.app.dbdesignbackend.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private StudentService studentService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.findStudentById(id));
    }

}
