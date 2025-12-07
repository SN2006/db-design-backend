package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CourseDTO;
import com.example.app.dbdesignbackend.dto.CreateCourseDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.dto.UpdateCourseDTO;
import com.example.app.dbdesignbackend.security.User;
import com.example.app.dbdesignbackend.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private CourseService courseService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CourseDTO>> findAll() {
        List<CourseDTO> courses = courseService.findAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/by-name/{name}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CourseDTO> findByName(@PathVariable String name) {
        CourseDTO course = courseService.findByName(name);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/by-name/{name}/groups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GroupDTO>> findAvailableGroupsForCourse(
            @PathVariable String name,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        List<GroupDTO> groups = courseService.findAvailableGroupsForCourse(name, user.getId());
        return ResponseEntity.ok(groups);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> createCourse(@RequestBody @Valid CreateCourseDTO createCourseDTO) {
        courseService.createCourse(createCourseDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/by-name/{courseName}/topics/add/{topicName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> addTopicToCourse(
            @PathVariable String courseName,
            @PathVariable String topicName
    ) {
        courseService.addTopicToCourse(courseName, topicName);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/by-name/{courseName}/topics/delete/{topicName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTopicToCourse(
            @PathVariable String courseName,
            @PathVariable String topicName
    ) {
        courseService.deleteTopicFromCourse(courseName, topicName);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/by-name/{courseName}/teachers/add/{teacherId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> addTeacherToCourse(
            @PathVariable String courseName,
            @PathVariable Integer teacherId
    ) {
        courseService.addTeacherToCourse(courseName, teacherId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/by-name/{courseName}/teachers/delete/{teacherId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTeacherToCourse(
            @PathVariable String courseName,
            @PathVariable Integer teacherId
    ) {
        courseService.deleteTeacherFromCourse(courseName, teacherId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/by-name/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> updateCourse(
            @PathVariable String name,
            @RequestBody @Valid UpdateCourseDTO updateCourseDTO
    ) {
        courseService.updateCourse(name, updateCourseDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/by-name/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable String name) {
        courseService.deleteCourse(name);
        return ResponseEntity.noContent().build();
    }

}
