package com.example.demo.Services.User;

import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    Page<User> getAllUserNotUserLogin(Integer page, String email);
    boolean forgetPassword(String email);
    boolean resetPassword(String confirmCode, String password);
    long countUser();
    Optional<User> getUserByEmail(String email);
    List<UserDTO> sortUser(String sort, List<UserDTO> userDTOList);
    User getUserById(Integer id) throws Exception;
    Page<User> searchUserAndPagination(String keyword, Integer page);
    String register(String email, String password, String cfPassword);
    String addNewUser(User user, MultipartFile image) throws IOException;
    String updateUser(User user, MultipartFile image) throws IOException;
    String deleteUser(Integer userId) throws IOException;
    String changePassword(Integer userId, String currentPassword, String newPassword, String confirmPassword);
    String updateUserLogin(User user, MultipartFile image) throws IOException;
}
