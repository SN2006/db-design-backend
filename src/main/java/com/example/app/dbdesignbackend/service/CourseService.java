package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.CourseDAO;
import com.example.app.dbdesignbackend.dto.CourseDTO;
import com.example.app.dbdesignbackend.dto.CreateCourseDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.dto.UpdateCourseDTO;
import com.example.app.dbdesignbackend.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseDAO courseDAO;

    public List<CourseDTO> findAll() {
        return courseDAO.getCourses();
    }

    public CourseDTO findByName(String name) {
        Optional<CourseDTO> courseDTO = courseDAO.getCourseByName(name);

        if (courseDTO.isEmpty()) {
            throw new EntityNotFoundException("Course with name " + name + " not found");
        }

        CourseDTO course = courseDTO.get();
        course.setTeachers(courseDAO.getTeachersByCourse(course.getId()));

        return course;
    }

    public List<GroupDTO> findAvailableGroupsForCourse(String courseName, Integer studentId) {
        return courseDAO.getAvailableGroupsForCourse(courseName, studentId);
    }

    public void createCourse(CreateCourseDTO createCourseDTO) {
        courseDAO.createCourse(createCourseDTO);
    }

    public void addTopicToCourse(String courseName, String topicName) {
        courseDAO.addTopicToCourse(courseName, topicName);
    }

    public void deleteTopicFromCourse(String courseName, String topicName) {
        courseDAO.deleteTopicFromCourse(courseName, topicName);
    }

    public void addTeacherToCourse(String courseName, int teacherId) {
        courseDAO.addTeacherToCourse(courseName, teacherId);
    }

    public void deleteTeacherFromCourse(String courseName, int teacherId) {
        courseDAO.deleteTeacherFromCourse(courseName, teacherId);
    }

    public void updateCourse(String name, UpdateCourseDTO updateCourseDTO) {
        if (courseDAO.getCourseByName(name).isEmpty()) {
            throw new EntityNotFoundException("Course with name " + name + " not found");
        }
        courseDAO.updateCourse(name, updateCourseDTO);
    }

    public void deleteCourse(String name) {
        if (courseDAO.getCourseByName(name).isEmpty()) {
            throw new EntityNotFoundException("Course with name " + name + " not found");
        }
        courseDAO.deleteCourse(name);
    }

}
