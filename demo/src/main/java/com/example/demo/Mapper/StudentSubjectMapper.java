package com.example.demo.Mapper;

import com.example.demo.DTOs.StudentSubjectDTO;
import com.example.demo.Entities.StudentSubject;

public class StudentSubjectMapper {

    public static StudentSubjectDTO mapFromEntityToDTO(StudentSubject studentSubject) {
        if(studentSubject == null) return null;
        return new StudentSubjectDTO(
                studentSubject.getId(),
                studentSubject.getStudentId(),
                studentSubject.getSubjectId(),
                studentSubject.getSemesterId(),
                studentSubject.getClassCode(),
                studentSubject.getPointProcess(),
                studentSubject.getPointFinal(),
                studentSubject.getPointLetter(),
                studentSubject.getStatus(),
                studentSubject.getCreateAt(),
                studentSubject.getUpdateAt(),
                studentSubject.getStudent(),
                studentSubject.getSubject(),
                studentSubject.getSemester()
        );
    }

    public static StudentSubject mapFromDTOToEntity(StudentSubjectDTO studentSubjectDTO) {
        return new StudentSubject(
                studentSubjectDTO.getId(),
                studentSubjectDTO.getStudentId(),
                studentSubjectDTO.getSubjectId(),
                studentSubjectDTO.getSemesterId(),
                studentSubjectDTO.getClassCode(),
                studentSubjectDTO.getPointProcess(),
                studentSubjectDTO.getPointFinal(),
                studentSubjectDTO.getPointLetter(),
                studentSubjectDTO.getStatus(),
                studentSubjectDTO.getCreateAt(),
                studentSubjectDTO.getUpdateAt(),
                studentSubjectDTO.getStudent(),
                studentSubjectDTO.getSubject(),
                studentSubjectDTO.getSemester()
        );
    }
}
