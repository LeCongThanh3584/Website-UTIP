package com.example.demo.Services.Class;

import com.example.demo.DTOs.ClassDTO;
import com.example.demo.Entities.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClassService {
    List<Class> getAllClass();
    Page<Class> getAllClassAndPagination(Integer page);
    Long countClass();
    String addNewClass(Class newClass);
    String updateClass(Class updateClass);
    Class getClassById(Integer id) throws Exception;
    Page<Class> searchClass(String keyword, Integer page);
    List<ClassDTO> sortClass(List<ClassDTO> classDTOListm, String sort);
    String deleteClass(Integer id);
    Page<Class> phanTrang(Pageable pageable);
}

