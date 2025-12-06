package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.ScheduleDAO;
import com.example.app.dbdesignbackend.dto.CreateScheduleDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleService {

    private ScheduleDAO scheduleDAO;

    public void createSchedule(CreateScheduleDTO createScheduleDTO) {
        scheduleDAO.createSchedule(createScheduleDTO);
    }

}
