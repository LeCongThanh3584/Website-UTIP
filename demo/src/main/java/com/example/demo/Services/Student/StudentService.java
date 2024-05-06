package com.example.demo.Services.Student;

import com.example.demo.DTOs.StudentDTO;
import com.example.demo.Entities.Class;
import com.example.demo.Entities.Department;
import com.example.demo.Entities.Role;
import com.example.demo.Entities.Student;
import com.example.demo.Enums.ERole;
import com.example.demo.Repository.ClassRepository;
import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Collator;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class StudentService implements IStudentService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final ClassRepository classRepository;

    private final DepartmentRepository departmentRepository;

    private final RoleRepository roleRepository;

    public StudentService(StudentRepository studentRepository,
                          ClassRepository classRepository,
                          DepartmentRepository departmentRepository,
                          RoleRepository roleRepository
    ) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Page<Student> getAllStudentAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Integer> getListQuantityStudentChart() {
        int quantityMale = studentRepository.getQuantityStudentMale();
        int quantityFeMale = studentRepository.getQuantityStudentFemale();
        int quantityLearn = studentRepository.getQuantityStudentLearn();
        int quantityReserve = studentRepository.getQuantityStudentReserve();
        int quantityOutLearn = studentRepository.getQuantityStudentOutLearn();
        return Arrays.asList(quantityMale, quantityFeMale, quantityLearn, quantityReserve, quantityOutLearn);
    }

    @Override
    public Student getStudentById(Integer id) throws Exception{
        Optional<Student> studentOptional = studentRepository.findById(id);
        if(studentOptional.isEmpty()) throw new Exception("Sinh viên không tồn tại");

        return studentOptional.get();
    }

    @Override
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public long getQuantityStudent() {
        return studentRepository.count();
    }

    @Override
    public Page<Student> searchStudent(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return studentRepository.searchStudent(keyword, pageable);
    }

    @Override
    public List<StudentDTO> sortStudent(List<StudentDTO> studentDTOList, String sort) {
        switch (sort) {
            case "name":
                //Sắp xếp theo tên của sinh viên
                Collator collator = Collator.getInstance(new Locale("vi"));
                Collections.sort(studentDTOList, new Comparator<StudentDTO>() {
                    @Override
                    public int compare(StudentDTO o1, StudentDTO o2) {
                        String fullName_1 = o1.getStudentName(); //Lấy họ tên của sinh viên 1
                        String fullName_2 = o2.getStudentName(); //Lấy họ tên của sinh viên 2
                        String name_1 = fullName_1.substring(fullName_1.lastIndexOf(" ") + 1); //Lấy ra tên sv1
                        String name_2 = fullName_2.substring(fullName_2.lastIndexOf(" ") + 1); //Lấy ra tên sv2
                        return collator.compare(name_1, name_2);
                    }
                });
                break;
            case "class":
                Collections.sort(studentDTOList, new Comparator<StudentDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(StudentDTO o1, StudentDTO o2) {
                        return collator.compare(o1.getStudentClass().getClassName(), o2.getStudentClass().getClassName());
                    }
                });
                break;
            case "address":
                Collections.sort(studentDTOList, new Comparator<StudentDTO>() {
                    @Override
                    public int compare(StudentDTO o1, StudentDTO o2) {
                        return o1.getAddress().compareTo(o2.getAddress());
                    }
                });
                break;
        }
        return studentDTOList;
    }

    @Override
    public String addNewStudent(Student student, MultipartFile image) throws IOException {

        Optional<Class> classOptional = classRepository.findById(student.getClassId());
        if(classOptional.isEmpty()) return "Lớp có id " + student.getClassId() + " không tồn tại";

        Optional<Department> departmentOptional = departmentRepository.findById(student.getDepartmentId());
        if(departmentOptional.isEmpty()) return "Khoa có id " + student.getDepartmentId() + " không tồn tại";

        Optional<Role> roleStudent = roleRepository.findByRoleName(ERole.STUDENT);
        if(roleStudent.isEmpty()) return "Role student không tồn tại";

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setStudentClass(classOptional.get());
        student.setDepartment(departmentOptional.get());
        student.setCreateAt(LocalDate.now());
        student.setUpdateAt(LocalDate.now());
        student.setRole(roleStudent.get());
        student.setCreditsAccumulate(0.0);
        student.setCreditsOwe(0.0);
        student.setCumulativeGPA(0.0);
        student.setEnable(true);

        String imageName = StringUtils.cleanPath(image.getOriginalFilename());
        student.setImage(imageName);

        studentRepository.save(student);

        classOptional.get().setSize(classOptional.get().getSize() + 1);  //Tăng sĩ số lớp mới lên 1
        classRepository.save(classOptional.get());

        String uploadDir = "images/student/id-" + student.getStudentId();

        FileUploadUtil.uploadImage(uploadDir, imageName, image);

        return "Thêm mới sinh viên thành công";
    }

    @Override
    public String updateStudent(Student student, MultipartFile image) throws IOException {

        Optional<Student> studentOptional = studentRepository.findById(student.getStudentId());
        if(studentOptional.isEmpty()) return "Học sinh có id " + student.getStudentId() + " không tồn tại";

        Optional<Class> classOptional = classRepository.findById(student.getClassId());
        if(classOptional.isEmpty()) return "Lớp có id " + student.getClassId() + " không tồn tại";

        Optional<Department> departmentOptional = departmentRepository.findById(student.getDepartmentId());
        if(departmentOptional.isEmpty()) return "Khoa có id " + student.getDepartmentId() + " không tồn tại";

        classOptional.get().setSize(classOptional.get().getSize() + 1); //Sĩ số lớp mới tăng lên 1
        classRepository.save(classOptional.get());

        studentOptional.get().getStudentClass().setSize(studentOptional.get().getStudentClass().getSize() - 1); //Sĩ số lớp cũ giảm đi 1
        classRepository.save(studentOptional.get().getStudentClass());

        if(student.getPassword().isEmpty()) {
            student.setPassword(studentOptional.get().getPassword());
        } else {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }

        student.setCumulativeGPA(studentOptional.get().getCumulativeGPA());
        student.setCreditsOwe(studentOptional.get().getCreditsOwe());
        student.setCreditsAccumulate(studentOptional.get().getCreditsAccumulate());
        student.setCreateAt(studentOptional.get().getCreateAt());
        student.setUpdateAt(LocalDate.now());
        student.setStudentClass(classOptional.get());
        student.setDepartment(departmentOptional.get());
        student.setRole(studentOptional.get().getRole());

        if(image.isEmpty()) {
            student.setImage(studentOptional.get().getImage());
            studentRepository.save(student);

            return "Cập nhật sinh viên có id " + student.getStudentId() + " thành công";
        }

        String imageName = StringUtils.cleanPath(image.getOriginalFilename());
        student.setImage(imageName);

        studentRepository.save(student);

        String uploadDir = "images/student/id-" + student.getStudentId();
        FileUploadUtil.uploadImage(uploadDir, imageName, image);

        return "Cập nhật sinh viên có id " + student.getStudentId() + " thành công";
    }

    @Override
    public String updateStudent(Student student) {  //Dành cho quyền student
        Optional<Student> studentOptional = studentRepository.findById(student.getStudentId());
        if(studentOptional.isEmpty()) return "Sinh viên không tồn tại để cập nhật";

        studentOptional.get().setStudentName(student.getStudentName());
        studentOptional.get().setPhoneNumber(student.getPhoneNumber());
        studentOptional.get().setAddress(student.getAddress());
        studentOptional.get().setDateOfBirth(student.getDateOfBirth());
        studentOptional.get().setGender(student.getGender());
        studentOptional.get().setUpdateAt(LocalDate.now());

        studentRepository.save(studentOptional.get());

        return "Cập nhật thông tin sinh viên thành công";
    }

    @Override
    public String changePassword(Integer studentId, String currentPassword, String newPassword, String confirmPassword) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if(studentOptional.isEmpty()) return "Sinh viên không tồn tại";

        if(!newPassword.equals(confirmPassword)) return "Mật khẩu mới và nhập lại mật khẩu mới không giống nhau";

        if(!passwordEncoder.matches(currentPassword, studentOptional.get().getPassword())) return "Mật khẩu hiện tại không chính xác";

        studentOptional.get().setPassword(passwordEncoder.encode(newPassword));
        studentOptional.get().setUpdateAt(LocalDate.now());

        studentRepository.save(studentOptional.get());
        return "Cập nhật mật khẩu thành công";
    }

    @Override
    public String deleteStudent(Integer id) throws IOException {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if(studentOptional.isEmpty()) return "Sinh viên có id " + id + " không tồn tại";

        studentOptional.get().getStudentClass().setSize(studentOptional.get().getStudentClass().getSize() - 1); //Giảm học sinh đi 1
        classRepository.save(studentOptional.get().getStudentClass());

        String imageDir = "images/student/id-" + id;

        FileUploadUtil.deleteImage(imageDir);

        studentRepository.deleteById(id);

        return "Xoá sinh viên có id "  + id + " thành công";
    }
}
