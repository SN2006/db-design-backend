package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.HomeworkSolutionDTO;
import com.example.app.dbdesignbackend.service.HomeworkSolutionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/homework-solutions")
@AllArgsConstructor
public class HomeworkSolutionController {

    private HomeworkSolutionService homeworkSolutionService;

    @GetMapping("/by-homework/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<List<HomeworkSolutionDTO>> findAllByHomeworkId(@PathVariable int homeworkId) {
        return ResponseEntity.ok(homeworkSolutionService.findAllByHomeworkId(homeworkId));
    }

}
