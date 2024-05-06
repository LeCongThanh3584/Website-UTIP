package com.example.demo.DTOs;

import com.example.demo.Entities.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SubjectDTO {
    private int subjectId;

    private String subjectName;

    private String subjectCode;

    private double courseCredits;

    private String description;

    private int departmentId;

    private int lecturerId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private List<StudentSubject> studentSubjectList;

    private List<Schedule> scheduleList;

    private Lecturer lecturer;

    private Department department;
}
