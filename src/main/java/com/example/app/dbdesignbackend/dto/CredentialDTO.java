package com.example.app.dbdesignbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDTO {
    @Email(message = "Invalid email format")
    @NotEmpty
    private  String email;
    @NotEmpty
    private String password;
}
