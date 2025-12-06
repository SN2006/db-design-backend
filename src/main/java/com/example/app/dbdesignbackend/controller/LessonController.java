package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.LessonDTO;
import com.example.app.dbdesignbackend.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {

    private LessonService lessonService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<List<LessonDTO>> getAllLessons() {
        return ResponseEntity.ok(lessonService.findAll());
    }

}
