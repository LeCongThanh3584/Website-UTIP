package com.example.demo.Mapper;

import com.example.demo.DTOs.ScheduleDTO;
import com.example.demo.Entities.Schedule;

public class ScheduleMapper {

    public static ScheduleDTO mapFromEntityToDTO(Schedule schedule) {
        return new ScheduleDTO(
                schedule.getScheduleId(),
                schedule.getSubjectId(),
                schedule.getTimeLearn(),
                schedule.getSchoolDay(),
                schedule.getSemesterId(),
                schedule.getMaxRegister(),
                schedule.getRegistered(),
                schedule.getClassCode(),
                schedule.getLocation(),
                schedule.getRegisStartTime(),
                schedule.getRegisEndTime(),
                schedule.getCreateAt(),
                schedule.getUpdateAt(),
                schedule.getSubject(),
                schedule.getSemester()
        );
    }

    public static Schedule mapFromDTOToEntity(ScheduleDTO scheduleDTO) {
        return new Schedule(
                scheduleDTO.getScheduleId(),
                scheduleDTO.getSubjectId(),
                scheduleDTO.getTimeLearn(),
                scheduleDTO.getSchoolDay(),
                scheduleDTO.getSemesterId(),
                scheduleDTO.getMaxRegister(),
                scheduleDTO.getRegistered(),
                scheduleDTO.getClassCode(),
                scheduleDTO.getLocation(),
                scheduleDTO.getRegisStartTime(),
                scheduleDTO.getRegisEndTime(),
                scheduleDTO.getCreateAt(),
                scheduleDTO.getUpdateAt(),
                scheduleDTO.getSubject(),
                scheduleDTO.getSemester()
        );
    }
}
