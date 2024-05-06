package com.example.demo.DTOs;

import com.example.demo.Entities.Schedule;
import com.example.demo.Entities.Semester;
import com.example.demo.Entities.StudentSubject;
import com.example.demo.Entities.TimeTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class SemesterDTO {
    private int semesterId;

    private String semesterName;

    private LocalDate startTime;

    private LocalDate endTime;

    private List<StudentSubject> studentSubjectList;

    private List<Schedule> scheduleList;

    private List<TimeTable> timeTableList;
}
