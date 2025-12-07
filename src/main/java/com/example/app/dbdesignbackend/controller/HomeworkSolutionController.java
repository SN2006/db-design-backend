package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.EvaluateHomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.HomeworkSolutionDTO;
import com.example.app.dbdesignbackend.dto.SendHomeworkSolutionDTO;
import com.example.app.dbdesignbackend.security.User;
import com.example.app.dbdesignbackend.service.HomeworkSolutionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/by-homework/{homeworkId}/my")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<List<HomeworkSolutionDTO>> findAllByHomeworkAndStudent(
            @PathVariable int homeworkId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(homeworkSolutionService.findAllByHomeworkAndStudent(
                homeworkId,
                user.getId()
        ));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<Void> sendHomeworkSolution(
            @RequestBody @Valid SendHomeworkSolutionDTO sendHomeworkSolutionDTO,
            Authentication authentication
            ) {
        User user = (User) authentication.getPrincipal();
        homeworkSolutionService.sendSolution(user.getId(), sendHomeworkSolutionDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/evaluate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> evaluateHomeworkSolution(
            @PathVariable Integer id,
            @RequestBody EvaluateHomeworkSolutionDTO evaluateHomeworkSolutionDTO
    ) {
        homeworkSolutionService.evaluateHomeworkSolution(id, evaluateHomeworkSolutionDTO);
        return ResponseEntity.ok().build();
    }

}
