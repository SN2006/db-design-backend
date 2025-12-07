package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.CertificateDAO;
import com.example.app.dbdesignbackend.dto.CertificateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CertificateService {

    private CertificateDAO certificateDAO;

    public List<CertificateDTO> getCertificatesForStudent(Integer studentId) {
        return certificateDAO.getCertificatesByStudentId(studentId);
    }

}
