package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private List<TopicDTO> topics;
}
