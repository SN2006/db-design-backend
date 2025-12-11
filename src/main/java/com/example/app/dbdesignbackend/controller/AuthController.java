package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CredentialDTO;
import com.example.app.dbdesignbackend.dto.LoginResponseDTO;
import com.example.app.dbdesignbackend.dto.RegistrationDTO;
import com.example.app.dbdesignbackend.security.Role;
import com.example.app.dbdesignbackend.security.User;
import com.example.app.dbdesignbackend.service.LoginService;
import com.example.app.dbdesignbackend.service.RegisterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private LoginService loginService;
    private RegisterService registerService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid CredentialDTO credentialDTO) {
        return ResponseEntity.ok().body(loginService.login(credentialDTO));
    }

    @PostMapping("/register/student")
    public ResponseEntity<Void> registerStudent(@RequestBody @Valid RegistrationDTO registrationDTO) {
        registerService.register(registrationDTO, Role.STUDENT);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<Void> registerTeacher(@RequestBody @Valid RegistrationDTO registrationDTO) {
        registerService.register(registrationDTO, Role.TEACHER);
        return ResponseEntity.ok().build();
    }

}
