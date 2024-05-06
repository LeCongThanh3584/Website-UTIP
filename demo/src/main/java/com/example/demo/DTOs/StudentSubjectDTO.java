package com.example.demo.DTOs;

import com.example.demo.Entities.Semester;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.StudentSubject;
import com.example.demo.Entities.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class StudentSubjectDTO {
    private int id;

    private int studentId;

    private int subjectId;

    private int semesterId;

    private String classCode;

    private Double pointProcess;

    private Double pointFinal;

    private String pointLetter;

    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private Student student;

    private Subject subject;

    private Semester semester;
}
