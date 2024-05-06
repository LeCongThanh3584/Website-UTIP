package com.example.demo.DTOs;

import com.example.demo.Entities.Department;
import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DepartmentDTO {
    private int departmentId;

    private String departmentCode;

    private String departmentName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private List<Student> studentList;

    private List<Lecturer> lecturerList;

    private List<Subject> subjectList;


}
