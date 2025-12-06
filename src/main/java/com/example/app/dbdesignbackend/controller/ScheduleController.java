package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CreateScheduleDTO;
import com.example.app.dbdesignbackend.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@AllArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> createSchedule(
            @RequestBody @Valid CreateScheduleDTO createScheduleDTO
    ) {
        scheduleService.createSchedule(createScheduleDTO);
        return ResponseEntity.ok().build();
    }

}
