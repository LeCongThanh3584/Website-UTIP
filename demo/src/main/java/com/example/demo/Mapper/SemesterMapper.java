package com.example.demo.Mapper;

import com.example.demo.DTOs.SemesterDTO;
import com.example.demo.Entities.Semester;

public class SemesterMapper {
    public static SemesterDTO mapFromEntityToDTO(Semester semester) {
        return new SemesterDTO(
                semester.getSemesterId(),
                semester.getSemesterName(),
                semester.getStartTime(),
                semester.getEndTime(),
                semester.getStudentSubjectList(),
                semester.getScheduleList(),
                semester.getTimeTableList()
        );
    }}
