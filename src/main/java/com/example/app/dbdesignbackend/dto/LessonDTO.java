package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDTO {
    private Integer id;
    private String name;
    private TopicDTO topic;
    private String description;
    private String duration;
}
