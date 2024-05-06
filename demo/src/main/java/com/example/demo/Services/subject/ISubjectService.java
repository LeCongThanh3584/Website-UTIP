package com.example.demo.Services.subject;

import com.example.demo.DTOs.SubjectDTO;
import com.example.demo.Entities.Subject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISubjectService {
    List<Subject> getAllSubject();
    Page<Subject> getAllSubjectAndPagination(Integer page);
    Subject getSubjectById(Integer id) throws Exception;
    Page<Subject> searchSubject(String keyword, Integer page);
    long getQuantitySubject();
    List<SubjectDTO> sortSubject(List<SubjectDTO> subjectDTOList, String sort);
    String addNewSubject(Subject subject);
    String updateSubject(Subject subject);
    String deleteSubject(Integer id);
}
