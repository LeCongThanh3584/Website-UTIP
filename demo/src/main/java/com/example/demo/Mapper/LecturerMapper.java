package com.example.demo.Mapper;

import com.example.demo.DTOs.LecturerDTO;
import com.example.demo.Entities.Lecturer;

public class LecturerMapper {
    public static LecturerDTO mapFromEntityToDTO(Lecturer lecturer) {
        return new LecturerDTO(
                lecturer.getLecturerId(),
                lecturer.getLecturerName(),
                lecturer.getLecturerCode(),
                lecturer.getEmail(),
                null,
                lecturer.getDateOfBirth(),
                lecturer.getGender(),
                lecturer.getPhoneNumber(),
                lecturer.getLevel(),
                lecturer.getAddress(),
                lecturer.getImage(),
                lecturer.isEnable(),
                lecturer.getDepartmentId(),
                lecturer.getRoleId(),
                lecturer.getCreateAt(),
                lecturer.getUpdateAt(),
                lecturer.getDepartment(),
                lecturer.getSubjectList(),
                lecturer.getRole()
        );
    }

    public static Lecturer mapFromDTOToEntity(LecturerDTO lecturerDTO) {
        return new Lecturer(
                lecturerDTO.getLecturerId(),
                lecturerDTO.getLecturerName(),
                lecturerDTO.getLecturerCode(),
                lecturerDTO.getEmail(),
                lecturerDTO.getPassword(),
                lecturerDTO.getDateOfBirth(),
                lecturerDTO.getGender(),
                lecturerDTO.getPhoneNumber(),
                lecturerDTO.getLevel(),
                lecturerDTO.getAddress(),
                lecturerDTO.getImage(),
                lecturerDTO.isEnable(),
                lecturerDTO.getDepartmentId(),
                lecturerDTO.getRoleId(),
                lecturerDTO.getCreateAt(),
                lecturerDTO.getUpdateAt(),
                lecturerDTO.getDepartment(),
                lecturerDTO.getSubjectList(),
                lecturerDTO.getRole()
        );
    }
}
