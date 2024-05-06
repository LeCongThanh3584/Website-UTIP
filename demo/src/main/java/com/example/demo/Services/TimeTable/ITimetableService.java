package com.example.demo.Services.TimeTable;

import com.example.demo.Entities.TimeTable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITimetableService {
    Page<TimeTable> findAllByStudentId(Integer studentId, Integer page);
    String addNewTimeTable(String classCode, Integer studentId);
    String deleteTimetable(String classCode, Integer studentId);
}
