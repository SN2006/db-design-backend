package com.example.app.dbdesignbackend.dto;

import com.example.app.dbdesignbackend.validation.annotation.IsoDuration;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateLessonDTO {
    @NotBlank(message = "Lesson name cannot be blank")
    private String lessonName;
    @NotBlank(message = "Topic name cannot be blank")
    private String topicName;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Duration cannot be null")
    @IsoDuration(message = "Duration must be in ISO 8601 format")
    private String duration;
}
