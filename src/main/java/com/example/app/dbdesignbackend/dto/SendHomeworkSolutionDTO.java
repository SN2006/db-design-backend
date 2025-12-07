package com.example.app.dbdesignbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendHomeworkSolutionDTO {
    @NotNull(message = "Homework ID cannot be null")
    private Integer homeworkId;
    @NotEmpty(message = "Solution cannot be empty")
    private String solution;
}
