package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    private Integer id;
    private String name;
    private String description;
    private String level;
    private String duration;
    private List<TopicDTO> topics;
    private List<TeacherDTO> teachers;
}
