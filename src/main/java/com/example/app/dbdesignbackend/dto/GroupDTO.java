package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDTO {
    private Integer id;
    private LocalDate startDate;
    private CourseDTO course;
    private Boolean isFinished;
}
