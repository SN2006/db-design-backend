package com.example.app.dbdesignbackend.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
