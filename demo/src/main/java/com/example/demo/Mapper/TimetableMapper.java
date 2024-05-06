package com.example.demo.Mapper;

import com.example.demo.DTOs.TimetableDTO;
import com.example.demo.Entities.TimeTable;

public class TimetableMapper {

    public static TimetableDTO mapFromEntityToDTO(TimeTable timeTable) {
        return new TimetableDTO(
                timeTable.getTimeTableId(),
                timeTable.getStudentId(),
                timeTable.getSubjectId(),
                timeTable.getTimeLearn(),
                timeTable.getSchoolDay(),
                timeTable.getClassCode(),
                timeTable.getLocation(),
                timeTable.getSemesterId(),
                timeTable.getCreateAt(),
                timeTable.getUpdateAt(),
                timeTable.getStudent(),
                timeTable.getSubject(),
                timeTable.getSemester()
        );
    }
}
