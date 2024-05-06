package com.example.demo.DTOs;

import com.example.demo.Entities.*;
import com.example.demo.Entities.Class;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class StudentDTO {
    private int studentId;

    private int classId;

    private int departmentId;

    private int roleId;

    private String studentName;

    private String studentCode;

    private String email;

    private String password;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private String statusLearn;

    private Double creditsOwe;

    private Double creditsAccumulate;

    private Double cumulativeGPA;

    private String address;

    private String phoneNumber;

    private String gender;

    private String image;

    private boolean enable;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private Class studentClass;

    private List<StudentSubject> studentSubjectList;

    private Department department;

    private Role role;
}
