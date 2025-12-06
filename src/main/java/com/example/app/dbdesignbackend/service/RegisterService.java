package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.RegisterDAO;
import com.example.app.dbdesignbackend.dto.RegistrationDTO;
import com.example.app.dbdesignbackend.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService {

    private RegisterDAO registerDAO;

    public void register(RegistrationDTO registrationDTO, Role role) {
        registerDAO.register(registrationDTO, role);
    }

}
