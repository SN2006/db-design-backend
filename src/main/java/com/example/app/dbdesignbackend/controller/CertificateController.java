package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CertificateDTO;
import com.example.app.dbdesignbackend.security.User;
import com.example.app.dbdesignbackend.service.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateController {

    private CertificateService certificateService;

    @GetMapping("/student/my")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<List<CertificateDTO>> getStudentCertificates(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(certificateService.getCertificatesForStudent(user.getId()));
    }

}
