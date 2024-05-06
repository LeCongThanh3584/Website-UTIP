package com.example.demo.Services.Schedule;

import com.example.demo.Entities.Schedule;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IScheduleService {
    Page<Schedule> getListScheduleAndPagination(Integer page);
    Page<Schedule> getListScheduleAndPaginationForStudent(Integer page);
    Page<Schedule> searchSchedule(String search, Integer page);
    Schedule getScheduleById(Integer id) throws Exception;
    String addNewSchedule(Schedule schedule);
    String updateSchedule(Schedule schedule);
    String deleteSchedule(Integer scheduleId);
}
