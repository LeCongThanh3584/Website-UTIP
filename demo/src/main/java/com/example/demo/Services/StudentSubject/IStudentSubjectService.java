package com.example.demo.Services.StudentSubject;

import com.example.demo.DTOs.StudentSubjectDTO;
import com.example.demo.Entities.StudentSubject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IStudentSubjectService {
    List<StudentSubject> getAllStudentSubject();
    Page<StudentSubject> getPointByLecturerId(Integer lecturerId, Integer page);
    List<StudentSubject> getAllByStudentId(Integer studentId);
    Page<StudentSubject> getAllPointAndPagination(Integer page);
    Page<StudentSubject> searchPoint(String keyword, Integer page);
    StudentSubject getPointById(Integer id) throws Exception;
    List<StudentSubjectDTO> sortPoint(List<StudentSubjectDTO> studentSubjectDTOList, String sort);
    String addNewStudentSubject(StudentSubject studentSubject);
    String updateStudentSubject(StudentSubject studentSubject);
    String deleteStudentSubject(Integer id);
}
