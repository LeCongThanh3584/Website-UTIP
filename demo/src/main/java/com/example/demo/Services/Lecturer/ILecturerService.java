package com.example.demo.Services.Lecturer;

import com.example.demo.DTOs.LecturerDTO;
import com.example.demo.Entities.Lecturer;
import org.springframework.data.domain.Page;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ILecturerService {
    List<Lecturer> getAllLecturer();
    Page<Lecturer> getAllLecturerAndPagination(Integer page);
    Lecturer getLecturerById(Integer id) throws Exception;
    Optional<Lecturer> getLecturerByEmail(String email);
    List<Integer> getListQuantityLecturer();
    long getQuantityLecturer();
    Page<Lecturer> searchLecturer(String keyword, Integer page);
    List<LecturerDTO> sortLecturer(List<LecturerDTO> lecturerDTOList, String sort);
    String addNewLecturer(Lecturer lecturer, MultipartFile image) throws IOException;
    String updateLecturer(Lecturer lecturer, MultipartFile image) throws IOException;
    String updateLecturer(Lecturer lecturer); //Cập nhật thông tin giảng viên từ quyền giảng viên
    String deleteLecturer(Integer id) throws IOException;
    String changePassword(Integer lecturerId, String currentPassword, String newPassword, String confirmPassword);
}
