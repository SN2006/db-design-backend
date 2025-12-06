package com.example.app.dbdesignbackend.dto;

import com.example.app.dbdesignbackend.validation.annotation.IsoPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseDTO {
    @NotBlank(message = "Course name must not be blank")
    private String name;
    @NotBlank(message = "Course description must not be blank")
    private String description;
    @NotNull(message = "Course level must not be null")
    @Pattern(message = "Course level must be one of: ПОЧАТКОВИЙ, ПРОСУНУТИЙ, ПРОФЕСІЙНИЙ",
             regexp = "ПОЧАТКОВИЙ|ПРОСУНУТИЙ|ПРОФЕСІЙНИЙ")
    private String level;
    @NotNull(message = "Course duration must not be null")
    @IsoPeriod(message = "Course duration must be in ISO 8601 format")
    private String duration;
}
