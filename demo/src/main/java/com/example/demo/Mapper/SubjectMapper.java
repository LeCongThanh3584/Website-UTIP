package com.example.demo.Mapper;

import com.example.demo.DTOs.SubjectDTO;
import com.example.demo.Entities.Subject;

public class SubjectMapper {

    public static SubjectDTO mapFromEntityToDTO(Subject subject) {
        return new SubjectDTO(
                subject.getSubjectId(),
                subject.getSubjectName(),
                subject.getSubjectCode(),
                subject.getCourseCredits(),
                subject.getDescription(),
                subject.getDepartmentId(),
                subject.getLecturerId(),
                subject.getCreateAt(),
                subject.getUpdateAt(),
                subject.getStudentSubjectList(),
                subject.getScheduleList(),
                subject.getLecturer(),
                subject.getDepartment()
        );
    }

    public static Subject mapFromDTOToEntity(SubjectDTO subjectDTO) {
        return new Subject(
                subjectDTO.getSubjectId(),
                subjectDTO.getSubjectName(),
                subjectDTO.getSubjectCode(),
                subjectDTO.getCourseCredits(),
                subjectDTO.getDescription(),
                subjectDTO.getDepartmentId(),
                subjectDTO.getLecturerId(),
                subjectDTO.getCreateAt(),
                subjectDTO.getUpdateAt(),
                subjectDTO.getStudentSubjectList(),
                subjectDTO.getScheduleList(),
                subjectDTO.getLecturer(),
                subjectDTO.getDepartment()
        );
    }
}
