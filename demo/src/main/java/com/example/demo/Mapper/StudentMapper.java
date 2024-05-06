package com.example.demo.Mapper;

import com.example.demo.DTOs.StudentDTO;
import com.example.demo.Entities.Student;

public class StudentMapper {
    public static Student mapFromDTOToEntity(StudentDTO studentDTO) {
        return new Student(
                studentDTO.getStudentId(),
                studentDTO.getClassId(),
                studentDTO.getDepartmentId(),
                studentDTO.getRoleId(),
                studentDTO.getStudentName(),
                studentDTO.getStudentCode(),
                studentDTO.getEmail(),
                studentDTO.getPassword(),
                studentDTO.getDateOfBirth(),
                studentDTO.getStatusLearn(),
                studentDTO.getCreditsOwe(),
                studentDTO.getCreditsAccumulate(),
                studentDTO.getCumulativeGPA(),
                studentDTO.getAddress(),
                studentDTO.getPhoneNumber(),
                studentDTO.getGender(),
                studentDTO.getImage(),
                studentDTO.isEnable(),
                studentDTO.getCreateAt(),
                studentDTO.getUpdateAt(),
                studentDTO.getStudentClass(),
                studentDTO.getStudentSubjectList(),
                studentDTO.getDepartment(),
                studentDTO.getRole()
        );
    }

    public static StudentDTO mapFromEntityToDTO(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getClassId(),
                student.getDepartmentId(),
                student.getRoleId(),
                student.getStudentName(),
                student.getStudentCode(),
                student.getEmail(),
                null,
                student.getDateOfBirth(),
                student.getStatusLearn(),
                student.getCreditsOwe(),
                student.getCreditsAccumulate(),
                student.getCumulativeGPA(),
                student.getAddress(),
                student.getPhoneNumber(),
                student.getGender(),
                student.getImage(),
                student.isEnable(),
                student.getCreateAt(),
                student.getUpdateAt(),
                student.getStudentClass(),
                student.getStudentSubjectList(),
                student.getDepartment(),
                student.getRole()
        );
    }
}
