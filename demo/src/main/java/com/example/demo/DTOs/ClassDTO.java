package com.example.demo.DTOs;

import com.example.demo.Entities.Class;
import com.example.demo.Entities.Student;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
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

public class ClassDTO {
    private int classId;

    @NotEmpty(message = "Tên lớp không được để trống")
    private String className;

    private int size;

    @NotEmpty(message = "Tên phòng học không được để trống")
    private String location;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private List<Student> studentList;
}
