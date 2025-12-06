package com.example.app.dbdesignbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateScheduleDTO {
    @NotNull(message = "Group ID cannot be null")
    private Integer groupId;
    @NotNull(message = "Teacher ID cannot be null")
    private Integer teacherId;
    @NotNull(message = "Lesson ID cannot be null")
    private Integer lessonId;
    @NotNull(message = "Schedule date cannot be null")
    @Future(message = "Schedule date must be in the future")
    private LocalDateTime scheduleDate;
}
