package com.example.demo.DTOs;

import com.example.demo.Entities.Role;
import com.example.demo.Enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RoleDTO {

    private int roleId;

    private ERole roleName;
}
