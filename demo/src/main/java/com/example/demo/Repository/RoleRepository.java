package com.example.demo.Repository;

import com.example.demo.Entities.Role;
import com.example.demo.Enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT role FROM Role role WHERE role.roleName = 'ADMIN' OR role.roleName = 'USER'")
    List<Role> getRoleAdminAndRoleUser();
    Optional<Role> findByRoleName(ERole roleName);
}
