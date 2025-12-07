package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.ScheduleDAO;
import com.example.app.dbdesignbackend.dto.CreateScheduleDTO;
import com.example.app.dbdesignbackend.dto.ScheduleDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private ScheduleDAO scheduleDAO;

    public List<ScheduleDTO> findAll() {
        return scheduleDAO.getAllSchedules();
    }

    public List<ScheduleDTO> findAllForGroup(Integer groupId) {
        return scheduleDAO.getAllSchedulesForGroup(groupId);
    }

    public void createSchedule(CreateScheduleDTO createScheduleDTO) {
        scheduleDAO.createSchedule(createScheduleDTO);
    }

}
