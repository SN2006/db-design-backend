package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.TeacherDTO;
import com.example.app.dbdesignbackend.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {

    private TeacherService teacherService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.findAll();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable int id) {
        TeacherDTO teacher = teacherService.findById(id);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/not-related-to-course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TeacherDTO>> getTeachersNotRelatedToCourse(@PathVariable int courseId) {
        List<TeacherDTO> teachers = teacherService.findAllNotRelatedToCourse(courseId);
        return ResponseEntity.ok(teachers);
    }

}
