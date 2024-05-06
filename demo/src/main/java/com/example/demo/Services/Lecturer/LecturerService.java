package com.example.demo.Services.Lecturer;

import com.example.demo.DTOs.LecturerDTO;
import com.example.demo.Entities.Department;
import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Role;
import com.example.demo.Enums.ERole;
import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.LecturerRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Collator;
import java.time.LocalDate;
import java.util.*;

@Service
public class LecturerService implements ILecturerService{

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Lecturer> getAllLecturer() {
        return lecturerRepository.findAll();
    }

    @Override
    public Page<Lecturer> getAllLecturerAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return lecturerRepository.findAll(pageable);
    }

    @Override
    public Lecturer getLecturerById(Integer id) throws Exception{
        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(id);
        if(lecturerOptional.isEmpty()) throw new Exception("Giảng viên không tồn tại");

        return lecturerOptional.get();
    }

    @Override
    public Optional<Lecturer> getLecturerByEmail(String email) {
        return lecturerRepository.findByEmail(email);
    }

    @Override
    public List<Integer> getListQuantityLecturer() {
        int quantityMaster = lecturerRepository.getQuantityLecturerMaster();
        int quantityDoctor = lecturerRepository.getQuantityLecturerDoctor();
        int quantityProfessor = lecturerRepository.getQuantityLecturerProfessor();
        int quantityAProfessor = lecturerRepository.getQuantityLecturerAProfessor();
        return Arrays.asList(quantityMaster, quantityDoctor, quantityProfessor, quantityAProfessor);
    }

    @Override
    public long getQuantityLecturer() {
        return lecturerRepository.count();
    }

    @Override
    public Page<Lecturer> searchLecturer(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return lecturerRepository.searchLecturer(keyword, pageable);
    }

    @Override
    public List<LecturerDTO> sortLecturer(List<LecturerDTO> lecturerDTOList, String sort) {
        switch (sort) {
            case "lecturerName":
                Collections.sort(lecturerDTOList, new Comparator<LecturerDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(LecturerDTO o1, LecturerDTO o2) {
                        String fullName_1 = o1.getLecturerName();
                        String fullName_2 = o2.getLecturerName();

                        String name_1 = fullName_1.substring(fullName_1.lastIndexOf(" ") + 1);
                        String name_2 = fullName_2.substring(fullName_2.lastIndexOf(" ") + 1);
                        return collator.compare(name_1, name_2);
                    }
                });
                break;
            case "lecturerCode":
                Collections.sort(lecturerDTOList, new Comparator<LecturerDTO>() {
                    @Override
                    public int compare(LecturerDTO o1, LecturerDTO o2) {
                        return o1.getLecturerCode().compareTo(o2.getLecturerCode());
                    }
                });
                break;
        }
        return lecturerDTOList;
    }

    @Override
    public String addNewLecturer(Lecturer lecturer, MultipartFile image) throws IOException {
        Optional<Department> departmentOptional = departmentRepository.findById(lecturer.getDepartmentId());
        if(departmentOptional.isEmpty()) return "Khoa có id " + lecturer.getDepartmentId() + " không tồn tại";

        Optional<Role> lecturerRole = roleRepository.findByRoleName(ERole.LECTURER);
        if(lecturerRole.isEmpty()) return "Role lecturer không tồn tại";

        lecturer.setPassword(passwordEncoder.encode(lecturer.getPassword()));
        lecturer.setDepartment(departmentOptional.get());
        lecturer.setCreateAt(LocalDate.now());
        lecturer.setUpdateAt(LocalDate.now());
        lecturer.setRole(lecturerRole.get());
        lecturer.setEnable(true);

        String imageName = StringUtils.cleanPath(image.getOriginalFilename());
        lecturer.setImage(imageName);

        lecturerRepository.save(lecturer);

        String imageDir = "images/lecturers/id-" + lecturer.getLecturerId();
        FileUploadUtil.uploadImage(imageDir, imageName, image);

        return "Thêm mới giảng viên thành công";
    }

    @Override
    public String updateLecturer(Lecturer lecturer, MultipartFile image) throws IOException {
        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(lecturer.getLecturerId());
        if(lecturerOptional.isEmpty()) return "Giảng viên có id " + lecturer.getLecturerId() + " không tồn tại";

        Optional<Department> departmentOptional = departmentRepository.findById(lecturer.getDepartmentId());
        if(departmentOptional.isEmpty()) return "Khoa có id " + lecturer.getDepartmentId() + " không tồn tại";

        lecturer.setCreateAt(lecturerOptional.get().getCreateAt());
        lecturer.setUpdateAt(LocalDate.now());
        lecturer.setDepartment(departmentOptional.get());
        lecturer.setRole(lecturerOptional.get().getRole());

        if(lecturer.getPassword().isEmpty()) {
            lecturer.setPassword(lecturerOptional.get().getPassword());
        } else {
            lecturer.setPassword(passwordEncoder.encode(lecturer.getPassword()));
        }

        if(image.isEmpty()) {
            lecturer.setImage(lecturerOptional.get().getImage()); //Không gửi ảnh lên thì sẽ lấy ảnh cũ

            lecturerRepository.save(lecturer);
            return "Cập nhật giảng viên có id " + lecturer.getLecturerId() + " thành công";
        }

        String imageName = StringUtils.cleanPath(image.getOriginalFilename());
        lecturer.setImage(imageName);

        lecturerRepository.save(lecturer);

        String imageDir = "images/lecturers/id-" + lecturer.getLecturerId();
        FileUploadUtil.uploadImage(imageDir, imageName, image);

        return "Cập nhật giảng viên có id " + lecturer.getLecturerId() + " thành công";
    }

    @Override
    public String updateLecturer(Lecturer lecturer) {
        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(lecturer.getLecturerId());
        if(lecturerOptional.isEmpty()) return "Giảng viên không tồn tại để cập nhật";

        lecturerOptional.get().setLecturerName(lecturer.getLecturerName());
        lecturerOptional.get().setDateOfBirth(lecturer.getDateOfBirth());
        lecturerOptional.get().setGender(lecturer.getGender());
        lecturerOptional.get().setPhoneNumber(lecturer.getPhoneNumber());
        lecturerOptional.get().setAddress(lecturer.getAddress());
        lecturerOptional.get().setUpdateAt(LocalDate.now());

        lecturerRepository.save(lecturerOptional.get());
        return "Cập nhật thông tin giảng viên thành công";
    }

    @Override
    public String deleteLecturer(Integer id) throws IOException {
        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(id);
        if(lecturerOptional.isEmpty()) return "Giảng viên có id " + id + " không tồn tại";

        String imageDir = "images/lecturers/id-" + id;
        FileUploadUtil.deleteImage(imageDir);

        lecturerRepository.deleteById(id);

        return "Xoá giảng viên thành công";
    }

    @Override
    public String changePassword(Integer lecturerId, String currentPassword, String newPassword, String confirmPassword) {
        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(lecturerId);
        if(lecturerOptional.isEmpty()) return "Giảng viên không tồn tại để cập nhật";

        if(!newPassword.equals(confirmPassword)) return "Mật khẩu mới và xác nhận mật khẩu mới không giống nhau";

        if (!passwordEncoder.matches(currentPassword, lecturerOptional.get().getPassword())) return "Mật khẩu hiện tại không đúng";

        lecturerOptional.get().setPassword(passwordEncoder.encode(newPassword));
        lecturerOptional.get().setUpdateAt(LocalDate.now());

        lecturerRepository.save(lecturerOptional.get());
        return "Cập nhật mật khẩu thành công";
    }
}
