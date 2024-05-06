package com.example.demo.Controllers;

import com.example.demo.DTOs.DepartmentDTO;
import com.example.demo.DTOs.LecturerDTO;
import com.example.demo.DTOs.StudentSubjectDTO;
import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.StudentSubject;
import com.example.demo.Mapper.DepartmentMapper;
import com.example.demo.Mapper.LecturerMapper;
import com.example.demo.Mapper.StudentSubjectMapper;
import com.example.demo.Services.Department.IDepartmentService;
import com.example.demo.Services.Lecturer.ILecturerService;
import com.example.demo.Services.StudentSubject.IStudentSubjectService;
import com.example.demo.Services.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LecturerController {

    private String messageResponse;

    private boolean showToast;

    private final ILecturerService iLecturerService;

    private final IDepartmentService iDepartmentService;
    private final IUserService iUserService;

    private final IStudentSubjectService iStudentSubjectService;

    public LecturerController(ILecturerService iLecturerService,
                              IDepartmentService iDepartmentService,
                              IUserService iUserService,
                              IStudentSubjectService iStudentSubjectService
    ) {
        this.iLecturerService = iLecturerService;
        this.iDepartmentService = iDepartmentService;
        this.iUserService = iUserService;
        this.iStudentSubjectService = iStudentSubjectService;
    }

    //    Quyền admin

    @GetMapping("/dash-board/list-lecturer")
    public String getListLecturerPage(Model model,
                  @RequestParam(required = false) String sort,
                  @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Lecturer> lecturerPage = iLecturerService.getAllLecturerAndPagination(page);

        List<LecturerDTO> lecturerDTOList = lecturerPage.stream().map(
                item -> LecturerMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            lecturerDTOList = iLecturerService.sortLecturer(lecturerDTOList, sort);
        }

        List<DepartmentDTO> departmentDTOList = iDepartmentService.getAllDepartment().stream().map(
                item -> DepartmentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(lecturerDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", lecturerPage.getTotalPages());
        model.addAttribute("listLecturers", lecturerDTOList);
        model.addAttribute("listDepartments", departmentDTOList);
        model.addAttribute("showToast", showToast);
        model.addAttribute("sort", sort);
        model.addAttribute("quantityLecturer", iLecturerService.getQuantityLecturer());
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("pageName", "admin/list-lecturer");
        model.addAttribute("active", 6);
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/dash-board/list-lecturer/search")
    public String searchLecturer(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer page)
    {
        Page<Lecturer> lecturerPage = iLecturerService.searchLecturer(keyword, page);

        List<LecturerDTO> lecturerDTOList = lecturerPage.stream().map(
                item -> LecturerMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            lecturerDTOList = iLecturerService.sortLecturer(lecturerDTOList, sort);
        }

        List<DepartmentDTO> departmentDTOList = iDepartmentService.getAllDepartment().stream().map(
                item -> DepartmentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if (lecturerDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", lecturerPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("quantityLecturer", iLecturerService.getQuantityLecturer());
        model.addAttribute("listLecturers", lecturerDTOList);
        model.addAttribute("listDepartments", departmentDTOList);
        model.addAttribute("pageName", "admin/list-lecturer");
        model.addAttribute("active", 6);
        return "/admin/dash-board";
    }

    @GetMapping("/get-lecturer-by-id/{id}")
    public ResponseEntity<?> getLecturerById(@PathVariable Integer id) {
        try {
            Lecturer lecturerReturn = iLecturerService.getLecturerById(id);
            LecturerDTO lecturerDTO = LecturerMapper.mapFromEntityToDTO(lecturerReturn);
            return new ResponseEntity<>(lecturerDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-quantity-lecturer-chart")
    public ResponseEntity<?> getQuantityLevelLecturer() {
        List<Integer> listLevelLecturer = iLecturerService.getListQuantityLecturer();

        return new ResponseEntity<>(listLevelLecturer, HttpStatus.OK);
    }

    @PostMapping("/add-new-lecturer")
    public String addNewLecturer(@ModelAttribute LecturerDTO lecturerDTO, @RequestParam("imageAddnew") MultipartFile image) throws IOException {
        Lecturer lecturerInput = LecturerMapper.mapFromDTOToEntity(lecturerDTO);
        messageResponse = iLecturerService.addNewLecturer(lecturerInput, image);
        showToast = true;
        return "redirect:/dash-board/list-lecturer";
    }

    @PostMapping("/update-lecturer")
    public String updateLecturer(@ModelAttribute LecturerDTO lecturerDTO, @RequestParam("imageUpdate") MultipartFile image) throws IOException {
        Lecturer lecturerInput = LecturerMapper.mapFromDTOToEntity(lecturerDTO);
        messageResponse = iLecturerService.updateLecturer(lecturerInput, image);
        showToast = true;
        return "redirect:/dash-board/list-lecturer";
    }

    @PostMapping("/delete-lecturer")
    public String deleteLecturer(@ModelAttribute LecturerDTO lecturerDTO) throws IOException{
        messageResponse = iLecturerService.deleteLecturer(lecturerDTO.getLecturerId());
        showToast = true;
        return "redirect:/dash-board/list-lecturer";
    }

//    Quyền giảng viên

    @GetMapping("/lecturer/infor")
    public String getLecturerInforPage(Model model) {

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("lecturerLogin", iLecturerService.getLecturerByEmail(authen.getName()).get());
        else model.addAttribute("lecturerLogin", null);

        model.addAttribute("active", 1);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("pageName", "/lecturer/infor-lecturer");
        showToast = false;
        return "/lecturer/dash-board";
    }

    @GetMapping("/lecturer/list-course")
    public String getListCourseLecturer(Model model) {

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();

        if(authen.isAuthenticated()) {
            Lecturer lecturer = iLecturerService.getLecturerByEmail(authen.getName()).get();
            model.addAttribute("lecturerLogin", lecturer);
            model.addAttribute("listCourse", lecturer.getSubjectList());
        }
        else {
            model.addAttribute("lecturerLogin", null);
            model.addAttribute("listCourse", null);
        }

        model.addAttribute("active", 2);
        model.addAttribute("pageName", "/lecturer/list-course");
        return "/lecturer/dash-board";
    }

    @GetMapping("/lecturer/sudent-course-grade")
    public String grading(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {

        Lecturer lecturer;
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) {
            lecturer = iLecturerService.getLecturerByEmail(authen.getName()).get();
            model.addAttribute("lecturerLogin", lecturer);
        }
        else {
            lecturer = null;
            model.addAttribute("lecturerLogin", lecturer);
        }

        Page<StudentSubject> studentSubjectPage = iStudentSubjectService.getPointByLecturerId(lecturer.getLecturerId(), page);

        List<StudentSubjectDTO> studentSubjectDTOList = studentSubjectPage.stream().map(
                item -> StudentSubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        model.addAttribute("active", 3);
        model.addAttribute("totalPage", studentSubjectPage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("listPoint", studentSubjectDTOList);
        model.addAttribute("pageName", "/lecturer/grading");
        showToast = false;
        return "/lecturer/dash-board";
    }

    @PostMapping("/lecturer/update-lecturer") //Cập nhật thông tin giảng viên của quyền giảng viên
    public String updateLecturer(@ModelAttribute LecturerDTO lecturerDTO) {
        Lecturer lecturer = LecturerMapper.mapFromDTOToEntity(lecturerDTO);

        messageResponse = iLecturerService.updateLecturer(lecturer);
        showToast = true;

        return "redirect:/lecturer/infor";
    }

    @PostMapping("/lecturer/change-password")
    public String updatePassword(
            @RequestParam("lecturerId") Integer lecturerId,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword
    ) {
        messageResponse = iLecturerService.changePassword(lecturerId, currentPassword, newPassword, confirmPassword);
        showToast = true;
        return "redirect:/lecturer/infor";
    }

    public void setMessageResponse(String messageResponse) {
        this.messageResponse = messageResponse;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }
}
