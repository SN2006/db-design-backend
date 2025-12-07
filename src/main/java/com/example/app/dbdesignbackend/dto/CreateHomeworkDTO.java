package com.example.app.dbdesignbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateHomeworkDTO {
    @NotNull(message = "Lesson ID cannot be null")
    private Integer lessonId;
    @NotNull(message = "Group ID cannot be null")
    private Integer groupId;
    @NotBlank(message = "Homework name cannot be blank")
    private String name;
    @NotNull(message = "Deadline cannot be null")
    @Future(message = "Deadline must be in the future")
    private LocalDate deadline;
}
