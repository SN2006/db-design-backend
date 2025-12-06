package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CreateTopicDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.service.TopicService;
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
@RequestMapping("/topics")
@AllArgsConstructor
public class TopicController {

    private TopicService topicService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TopicDTO>> getTopics() {
        return ResponseEntity.ok(topicService.findAll());
    }

    @GetMapping("/not-related-to-course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TopicDTO>> getTopicsNotRelatedToCourse(@PathVariable int courseId) {
        return ResponseEntity.ok(topicService.findAllNotRelatedToCourse(courseId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable int id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> createTopic(
            @RequestBody @Valid CreateTopicDTO topicDTO
    ) {
        topicService.createTopic(topicDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> updateTopic(
            @PathVariable int id,
            @RequestBody @Valid CreateTopicDTO topicDTO
    ) {
        topicService.updateTopic(id, topicDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTopic(@PathVariable int id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

}
