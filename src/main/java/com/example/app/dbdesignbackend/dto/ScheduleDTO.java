package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDTO {
    private Integer id;
    private LocalDateTime dateTime;
    private LessonDTO lesson;
    private GroupDTO group;
    private TeacherDTO teacher;
}
