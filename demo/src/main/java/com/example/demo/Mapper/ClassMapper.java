package com.example.demo.Mapper;

import com.example.demo.DTOs.ClassDTO;
import com.example.demo.Entities.Class;

public class ClassMapper {
    public static ClassDTO mapFromEntityToDTO(Class classes) {
        return new ClassDTO(
                classes.getClassId(),
                classes.getClassName(),
                classes.getSize(),
                classes.getLocation(),
                classes.getCreateAt(),
                classes.getUpdateAt(),
                classes.getStudentList()
        );
    }

    public static Class mapFromDTOToEntity(ClassDTO classDTO) {
        return new Class(
                classDTO.getClassId(),
                classDTO.getClassName(),
                classDTO.getSize(),
                classDTO.getLocation(),
                classDTO.getCreateAt(),
                classDTO.getUpdateAt(),
                classDTO.getStudentList()
        );
    }

    public static ClassDTO mapClassToClassResponse(Class classes) {
        if(classes == null) return null;

        return new ClassDTO(
                classes.getClassId(),
                classes.getClassName(),
                classes.getSize(), null, null, null, null
        );
    }
}
