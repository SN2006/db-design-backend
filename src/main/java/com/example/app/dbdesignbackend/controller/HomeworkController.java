package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CreateHomeworkDTO;
import com.example.app.dbdesignbackend.dto.HomeworkDTO;
import com.example.app.dbdesignbackend.service.HomeworkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/homeworks")
@AllArgsConstructor
public class HomeworkController {

    private HomeworkService homeworkService;

    @GetMapping("/by-group/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HomeworkDTO>> findAllByGroupId(
            @PathVariable Integer groupId
    ) {
        return ResponseEntity.ok(homeworkService.findAllByGroupId(groupId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> createHomework(
            @RequestBody @Valid CreateHomeworkDTO createHomeworkDTO
            ) {
        homeworkService.createHomework(createHomeworkDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> updateHomework(
            @PathVariable Integer homeworkId,
            @RequestBody @Valid CreateHomeworkDTO createHomeworkDTO
    ) {
        homeworkService.updateHomework(homeworkId, createHomeworkDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteHomework(
            @PathVariable Integer homeworkId
    ) {
        homeworkService.deleteHomework(homeworkId);
        return ResponseEntity.noContent().build();
    }

}
