package com.example.demo.Services.Role;

import com.example.demo.Entities.Role;
import com.example.demo.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getRoleAdminAndRoleUser() {
        return roleRepository.getRoleAdminAndRoleUser();
    }
}
