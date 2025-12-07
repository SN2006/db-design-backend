package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CreateLessonDTO;
import com.example.app.dbdesignbackend.dto.LessonDTO;
import com.example.app.dbdesignbackend.dto.UpdateLessonDTO;
import com.example.app.dbdesignbackend.service.LessonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> createLesson(
            @RequestBody @Valid CreateLessonDTO createLessonDTO
    ) {
        lessonService.createLesson(createLessonDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/by-name/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> updateLessonByName(
            @PathVariable String name,
            @RequestBody @Valid UpdateLessonDTO updateLessonDTO
    ) {
        lessonService.updateLessonByName(name, updateLessonDTO);
        return ResponseEntity.ok().build();
    }

}
