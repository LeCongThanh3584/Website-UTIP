package com.example.demo.Services.User;

import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.*;
import com.example.demo.Enums.ERole;
import com.example.demo.Repository.*;
import com.example.demo.Services.email.EmailService;
import com.example.demo.Utils.FileUploadUtil;
import com.example.demo.Utils.GenerateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerifyCodeRepository verifyCodeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public Page<User> getAllUserNotUserLogin(Integer page, String email) {
        Pageable pageable = PageRequest.of(page, 5);
        return userRepository.findAllByEmailNot(email, pageable);
    }

    @Override
    public boolean forgetPassword(String email) {  //Gửi mã code về email
        Optional<User> userOptional = userRepository.findByEmail(email);
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        Optional<Lecturer> lecturerOptional = lecturerRepository.findByEmail(email);

        if(userOptional.isEmpty() && studentOptional.isEmpty() && lecturerOptional.isEmpty()) return false;
        else {  //Email đã tồn tại một trong 3 bảng user, student, lecturer

            String confirmCode = GenerateCodeUtil.generateCode();
            VerifyCode verifyCode = new VerifyCode();

            verifyCode.setEmail(email);
            verifyCode.setConfirmCode(confirmCode);
            verifyCode.setExpiredTime(LocalDateTime.now().plusMinutes(3));
            verifyCode.setCreateAt(LocalDate.now());
            verifyCode.setUpdateAt(LocalDate.now());

            emailService.sendEmail(email, "Mã xác nhận lấy lại mật khẩu", confirmCode);

            verifyCodeRepository.save(verifyCode);

            return true;
        }
    }

    @Override
    public boolean resetPassword(String confirmCode, String password) {  //Xử lý đặt lại mật khẩu
        Optional<VerifyCode> verifyCodeOptional = verifyCodeRepository.findByConfirmCode(confirmCode);
        if(verifyCodeOptional.isEmpty()) return false;

        if(verifyCodeOptional.get().getExpiredTime().isBefore(LocalDateTime.now())) return false;

        Optional<User> userOptional = userRepository.findByEmail(verifyCodeOptional.get().getEmail());
        if(userOptional.isPresent()) {
            userOptional.get().setPassword(passwordEncoder.encode(password));
            userOptional.get().setUpdateAt(LocalDate.now());
            userRepository.save(userOptional.get());
            return true;
        }

        Optional<Student> studentOptional = studentRepository.findByEmail(verifyCodeOptional.get().getEmail());
        if(studentOptional.isPresent()) {
            studentOptional.get().setPassword(passwordEncoder.encode(password));
            studentOptional.get().setUpdateAt(LocalDate.now());
            studentRepository.save(studentOptional.get());
            return true;
        }

        Optional<Lecturer> lecturerOptional = lecturerRepository.findByEmail(verifyCodeOptional.get().getEmail());
        if(lecturerOptional.isPresent()) {
            lecturerOptional.get().setPassword(passwordEncoder.encode(password));
            lecturerOptional.get().setUpdateAt(LocalDate.now());
            lecturerRepository.save(lecturerOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public long countUser() {
        return userRepository.count();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> sortUser(String sort, List<UserDTO> userDTOList) {
        switch (sort) {
            case "email":
                Collections.sort(userDTOList, new Comparator<UserDTO>() {
                    @Override
                    public int compare(UserDTO o1, UserDTO o2) {
                        return o1.getEmail().compareTo(o2.getEmail());
                    }
                });
                break;
            case "userName":
                Collections.sort(userDTOList, new Comparator<UserDTO>() {
                    @Override
                    public int compare(UserDTO o1, UserDTO o2) {
                        return o1.getFullName().compareTo(o2.getFullName());
                    }
                });
                break;
        }
        return userDTOList;
    }

    @Override
    public User getUserById(Integer id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) throw new Exception("Người dùng không tồn tại");

        return userOptional.get();
    }

    @Override
    public Page<User> searchUserAndPagination(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return userRepository.searchUserAndPagination(keyword, pageable);
    }

    @Override
    public String register(String email, String password, String cfPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) return "Email đã tồn tại";

        if(!password.equals(cfPassword)) return "Mật khẩu và xác nhận mật khẩu không giống nhau";

        Optional<Role> roleOptional = roleRepository.findByRoleName(ERole.USER);
        if(roleOptional.isEmpty()) return "Vai trò người dùng không tồn tại";

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateAt(LocalDate.now());
        user.setUpdateAt(LocalDate.now());
        user.setEnable(true);
        user.setRole(roleOptional.get());

        userRepository.save(user);
        return "Đăng ký tài khoản thành công";
    }

    @Override
    public String addNewUser(User user, MultipartFile image) throws IOException {
        Optional<Role> roleOptional = roleRepository.findById(user.getRoleId());
        if(roleOptional.isEmpty()) return "Vai trò trống, không thể thêm";

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()) return "Email đã tồn tại, hãy thử email khác";

        user.setRole(roleOptional.get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnable(true);
        user.setCreateAt(LocalDate.now());
        user.setUpdateAt(LocalDate.now());

        String imageName = StringUtils.cleanPath(image.getOriginalFilename());
        user.setImage(imageName);

        userRepository.save(user);

        String uploadDir = "images/user/id-" + user.getUserId();

        FileUploadUtil.uploadImage(uploadDir, imageName, image);

        return "Thêm mới tài khoản thành công";
    }

    @Override
    public String updateUser(User user, MultipartFile image) throws IOException {
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if(userOptional.isEmpty()) return "Tài khoản người dùng không tồn tại để cập nhật";

        Optional<Role> roleOptional = roleRepository.findById(user.getRoleId());
        if(roleOptional.isEmpty()) return "Vai trò không tồn tại";

        if(user.getPassword().isEmpty()) {
            user.setPassword(userOptional.get().getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.setRole(roleOptional.get());
        user.setCreateAt(userOptional.get().getCreateAt());
        user.setUpdateAt(LocalDate.now());

        if(image.isEmpty()) user.setImage(userOptional.get().getImage());
        else {
            String imageName = StringUtils.cleanPath(image.getOriginalFilename());
            user.setImage(imageName);

            String uploadDir = "images/user/id-" + user.getUserId();

            FileUploadUtil.uploadImage(uploadDir, imageName, image);
        }

        userRepository.save(user);

        return "Cập nhật thông tin tài khoản thành công";
    }

    @Override
    public String deleteUser(Integer userId) throws IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) return "Tài khoản không tồn tại để xoá";

        userRepository.deleteById(userId);

        String imageDir = "images/user/id-" + userId;

        FileUploadUtil.deleteImage(imageDir);

        return "Xoá tài khoản có id " + userId + " thành công";
    }

    @Override
    public String changePassword(Integer userId, String currentPassword, String newPassword, String confirmPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) return "Tài khoản không tồn tại để cập nhật mật khẩu";

        if(!newPassword.equals(confirmPassword)) return "Mật khẩu và nhập lại mật khẩu không giống nhau";

        if(!passwordEncoder.matches(currentPassword, userOptional.get().getPassword())) return "Mật khẩu hiện tại không đúng";

        userOptional.get().setPassword(passwordEncoder.encode(newPassword));
        userOptional.get().setUpdateAt(LocalDate.now());
        userRepository.save(userOptional.get());

        return "Cập nhật mật khẩu thành công";
    }

    @Override
    public String updateUserLogin(User user, MultipartFile image) throws IOException {
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if(userOptional.isEmpty()) return "Tài khoản đăng nhập có id " + user.getUserId() + " không tồn tại";

        user.setCreateAt(userOptional.get().getCreateAt());
        user.setUpdateAt(LocalDate.now());
        user.setEnable(true);
        user.setRole(userOptional.get().getRole());
        user.setPassword(userOptional.get().getPassword());

        if(image.isEmpty()) {
            user.setImage(userOptional.get().getImage());
        } else {
            String imageName = StringUtils.cleanPath(image.getOriginalFilename());
            user.setImage(imageName);

            String imageDir = "images/user/id-" + user.getUserId();

            FileUploadUtil.uploadImage(imageDir, imageName, image);
        }

        userRepository.save(user);
        return "Cập nhật thông tin tài khoản đăng nhập thành công";
    }
}
