package com.example.demo.Services.Student;

import com.example.demo.DTOs.StudentDTO;
import com.example.demo.Entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IStudentService {
    List<Student> getAllStudent();
    Page<Student> getAllStudentAndPagination(Integer page);
    List<Integer> getListQuantityStudentChart();
    Student getStudentById(Integer id) throws Exception;
    Optional<Student> getStudentByEmail(String email);
    long getQuantityStudent();
    Page<Student> searchStudent(String keyword, Integer page);
    List<StudentDTO> sortStudent(List<StudentDTO> studentDTOList, String sort);
    String addNewStudent(Student student, MultipartFile image) throws IOException;
    String updateStudent(Student student, MultipartFile image) throws IOException; //Update student của admin
    String updateStudent(Student student); //Update student của cá nhân student
    String changePassword(Integer studentId, String currentPassword, String newPassword, String confirmPassword);
    String deleteStudent(Integer id) throws IOException;
}
