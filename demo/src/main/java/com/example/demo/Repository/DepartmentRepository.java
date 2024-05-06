package com.example.demo.Repository;

import com.example.demo.Entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("SELECT depart FROM Department depart WHERE depart.departmentName LIKE %?1% " +
            "OR depart.departmentCode LIKE %?1%")
    Page<Department> searchDepartment(String keyword, Pageable pageable);
}
