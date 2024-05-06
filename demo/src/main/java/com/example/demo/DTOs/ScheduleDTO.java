package com.example.demo.DTOs;

import com.example.demo.Entities.Schedule;
import com.example.demo.Entities.Semester;
import com.example.demo.Entities.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ScheduleDTO {
    private int scheduleId;

    private int subjectId;

    private String timeLearn;

    private String schoolDay;

    private int semesterId;

    private int maxRegister;

    private int registered;

    private String classCode;

    private String location;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate regisStartTime;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate regisEndTime;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private Subject subject;

    private Semester semester;
}
