package com.example.demo.DTOs;

import com.example.demo.Entities.Department;
import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Role;
import com.example.demo.Entities.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class LecturerDTO {
    private int lecturerId;

    private String lecturerName;

    private String lecturerCode;

    private String email;

    private String password;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    private String level;

    private String address;

    private String image;

    private boolean enable;

    private int departmentId;

    private int roleId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private Department department;

    private List<Subject> subjectList;

    private Role role;
}
