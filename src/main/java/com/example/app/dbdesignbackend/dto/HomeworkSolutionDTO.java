package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeworkSolutionDTO {
    private Integer id;
    private StudentDTO student;
    private HomeworkDTO homework;
    private String solution;
    private String status;
    private Integer grade;
    private String feedback;
}
