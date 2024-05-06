package com.example.demo.Services.Department;

import com.example.demo.DTOs.DepartmentDTO;
import com.example.demo.Entities.Department;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDepartmentService {
    List<Department> getAllDepartment();
    Page<Department> getAllDepartmentAndPagination(Integer page);
    Department getDepartmentById(Integer id) throws Exception;
    Page<Department> getDepartmentBySearch(String keyword, Integer page);
    long getQuantityDepartment();
    List<DepartmentDTO> sortDepartment(List<DepartmentDTO> departmentDTOList , String sort);
    String addNewDepartment(Department newDepartment);
    String updateDepartment(Department updateDepartment);
    String deleteDepartment(Integer id);
}
