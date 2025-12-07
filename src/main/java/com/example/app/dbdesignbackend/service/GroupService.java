package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.CourseDAO;
import com.example.app.dbdesignbackend.dao.GroupDAO;
import com.example.app.dbdesignbackend.dto.AverageGradeDTO;
import com.example.app.dbdesignbackend.dto.CreateGroupDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.dto.TeacherDTO;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {

    private GroupDAO groupDAO;
    private CourseDAO courseDAO;

    public List<GroupDTO> findAll() {
        return groupDAO.getAllGroups();
    }

    public List<GroupDTO> findAllForTeacher(Integer teacherId) {
        return groupDAO.getAllGroupsForTeacher(teacherId);
    }

    public List<GroupDTO> findAllForStudent(Integer studentId) {
        return groupDAO.getAllGroupsForStudent(studentId);
    }

    public List<GroupDTO> findAllAvailableForStudent(Integer studentId) {
        return groupDAO.getAvailableGroupsForStudent(studentId);
    }

    public List<TeacherDTO> findAllTeachersForGroup(Integer groupId) {
        return groupDAO.getTeachersByGroup(groupId);
    }

    public AverageGradeDTO findAverageGrade(Integer studentId, Integer groupId) {
        return groupDAO.getAverageGrade(studentId, groupId);
    }

    public void createGroup(CreateGroupDTO createGroupDTO) {
        if (courseDAO.getCourseByName(createGroupDTO.getCourseName()).isEmpty()) {
            throw new BadRequestException("Course with name " + createGroupDTO.getCourseName() + " does not exist");
        }
        groupDAO.createGroup(createGroupDTO);
    }

    public void joinGroup(Integer studentId, Integer groupId) {
        groupDAO.joinGroup(studentId, groupId);
    }

}
