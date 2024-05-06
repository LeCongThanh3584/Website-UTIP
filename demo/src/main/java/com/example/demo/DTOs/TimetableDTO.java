package com.example.demo.DTOs;

import com.example.demo.Entities.Semester;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.Subject;
import com.example.demo.Entities.TimeTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TimetableDTO {
    private int timeTableId;

    private int studentId;

    private int subjectId;

    private String timeLearn;

    private String schoolDay;

    private String classCode;

    private String location;

    private int semesterId;

    private LocalDate createAt;

    private LocalDate updateAt;

    private Student student;

    private Subject subject;

    private Semester semester;
}
