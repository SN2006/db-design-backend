package com.example.app.dbdesignbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTopicDTO {
    @NotBlank(message = "Topic name must not be blank")
    private String name;
}
