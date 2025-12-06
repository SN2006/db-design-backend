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
public class CreateGroupDTO {
    @NotBlank(message = "Course name must not be blank")
    private String courseName;
    @NotNull(message = "Start date must not be null")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;
}
