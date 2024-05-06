package com.example.demo.Mapper;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.Entities.Role;

public class RoleMapper {

    public static RoleDTO mapFromEntityToDTO(Role role) {
        return new RoleDTO(
                role.getRoleId(),
                role.getRoleName()
        );
    }
}
