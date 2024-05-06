package com.example.demo.Services.Department;

import com.example.demo.DTOs.DepartmentDTO;
import com.example.demo.Entities.Department;
import com.example.demo.Repository.DepartmentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentSerVice implements IDepartmentService{
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentSerVice(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<Department> getAllDepartmentAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page,2);
        return departmentRepository.findAll(pageable);
    }

    @Override
    public Department getDepartmentById(Integer id) throws Exception {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if(departmentOptional.isEmpty()) throw new Exception("Khoa không tồn tại");

        return departmentOptional.get();
    }

    @Override
    public Page<Department> getDepartmentBySearch(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return departmentRepository.searchDepartment(keyword, pageable);
    }

    @Override
    public long getQuantityDepartment() {
        return departmentRepository.count();
    }

    @Override
    public List<DepartmentDTO> sortDepartment(List<DepartmentDTO> departmentDTOList, String sort) {
        switch (sort) {
            case "departmentName":
                Collections.sort(departmentDTOList, new Comparator<DepartmentDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(DepartmentDTO o1, DepartmentDTO o2) {
                        return collator.compare(o1.getDepartmentName(), o2.getDepartmentName());
                    }
                });
                break;
            case "departmentCode":
                Collections.sort(departmentDTOList, new Comparator<DepartmentDTO>() {
                    @Override
                    public int compare(DepartmentDTO o1, DepartmentDTO o2) {
                        return o1.getDepartmentCode().compareTo(o2.getDepartmentCode());
                    }
                });
                break;
        }
        return departmentDTOList;
    }

    @Override
    public String addNewDepartment(Department newDepartment) {
        newDepartment.setCreateAt(LocalDate.now());
        newDepartment.setUpdateAt(LocalDate.now());

        departmentRepository.save(newDepartment);
        return "Thêm mới khoa thành công";
    }

    @Override
    public String updateDepartment(Department updateDepartment) {
        Optional<Department> departmentOptional = departmentRepository.findById(updateDepartment.getDepartmentId());
        if(departmentOptional.isEmpty()) return "Khoa có id " + updateDepartment.getDepartmentId() + " không tồn tại";

        departmentOptional.get().setDepartmentName(updateDepartment.getDepartmentName());
        departmentOptional.get().setDepartmentCode(updateDepartment.getDepartmentCode());
        departmentOptional.get().setUpdateAt(LocalDate.now());

        departmentRepository.save(departmentOptional.get());

        return "Cập nhật khoa có id " + updateDepartment.getDepartmentId() + " thành công";
    }

    @Override
    public String deleteDepartment(Integer id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if(departmentOptional.isEmpty()) return "Khoa có id " + id + " không tồn tại";

        departmentRepository.deleteById(id);
        return "Xoá khoa có id " + id + " thành công";
    }
}
