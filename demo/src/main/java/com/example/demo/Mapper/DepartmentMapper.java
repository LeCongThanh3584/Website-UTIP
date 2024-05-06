package com.example.demo.Mapper;

import com.example.demo.DTOs.DepartmentDTO;
import com.example.demo.Entities.Department;

public class DepartmentMapper {
    public static Department mapFromDTOToEntity(DepartmentDTO departmentDTO) {
        return new Department(
                departmentDTO.getDepartmentId(),
                departmentDTO.getDepartmentCode(),
                departmentDTO.getDepartmentName(),
                departmentDTO.getCreateAt(),
                departmentDTO.getUpdateAt(),
                departmentDTO.getStudentList(),
                departmentDTO.getLecturerList(),
                departmentDTO.getSubjectList()
        );
    }

    public static DepartmentDTO mapFromEntityToDTO(Department department) {
        if (department == null) return null;

        return new DepartmentDTO(
                department.getDepartmentId(),
                department.getDepartmentCode(),
                department.getDepartmentName(),
                department.getCreateAt(),
                department.getUpdateAt(),
                department.getStudentList(),
                department.getLecturerList(),
                department.getSubjectList()
        );

    }

    public static DepartmentDTO mapDepartmentToDepartmentResponse(Department department) {
        if (department == null) return null;

        return new DepartmentDTO(
                department.getDepartmentId(), null,
                department.getDepartmentName(),
                null, null, null, null, null
        );
    }
}
