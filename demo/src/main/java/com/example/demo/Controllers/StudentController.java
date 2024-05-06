package com.example.demo.Controllers;

import com.example.demo.DTOs.*;
import com.example.demo.Entities.Schedule;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.TimeTable;
import com.example.demo.Mapper.*;
import com.example.demo.Services.Class.IClassService;
import com.example.demo.Services.Department.IDepartmentService;
import com.example.demo.Services.Schedule.IScheduleService;
import com.example.demo.Services.Student.IStudentService;
import com.example.demo.Services.StudentSubject.IStudentSubjectService;
import com.example.demo.Services.TimeTable.ITimetableService;
import com.example.demo.Services.User.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.apache.catalina.util.Introspection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {
    private boolean showToast = false;
    private String messageResponse = "";
    private final IStudentService iStudentService;
    private final IClassService iClassService;
    private final IDepartmentService iDepartmentService;
    private final IUserService iUserService;
    private final ITimetableService iTimetableService;
    private final IScheduleService iScheduleService;
   private final IStudentSubjectService iStudentSubjectService;

    public StudentController(IStudentService iStudentService,
                             IClassService iClassService,
                             IDepartmentService iDepartmentService,
                             IUserService iUserService,
                             ITimetableService iTimetableService,
                             IScheduleService iScheduleService,
                             IStudentSubjectService iStudentSubjectService
    ) {
        this.iStudentService = iStudentService;
        this.iClassService = iClassService;
        this.iDepartmentService = iDepartmentService;
        this.iUserService = iUserService;
        this.iTimetableService = iTimetableService;
        this.iScheduleService = iScheduleService;
        this.iStudentSubjectService = iStudentSubjectService;
    }

    //Quyền admin

    @GetMapping("/dash-board/list-student")
    public String getListStudentPage(Model model,
                     @RequestParam(required = false) String sort,
                     @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Student> studentPage = iStudentService.getAllStudentAndPagination(page);

        List<StudentDTO> studentDTOList = studentPage.stream().map(
                item -> StudentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            studentDTOList = iStudentService.sortStudent(studentDTOList, sort);
        }

        List<ClassDTO> classDTOList = iClassService.getAllClass().stream().map(
                item -> ClassMapper.mapClassToClassResponse(item)
        ).collect(Collectors.toList());

        List<DepartmentDTO> departmentDTOList = iDepartmentService.getAllDepartment().stream().map(
                item -> DepartmentMapper.mapDepartmentToDepartmentResponse(item)
        ).collect(Collectors.toList());

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        if(studentDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        model.addAttribute("active", 2);
        model.addAttribute("totalPage", studentPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("listStudent", studentDTOList);
        model.addAttribute("listClass", classDTOList);
        model.addAttribute("pageNumber", page);
        model.addAttribute("showToast", showToast);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("listDepartment", departmentDTOList);
        model.addAttribute("quantityStudent", iStudentService.getQuantityStudent());
        model.addAttribute("pageName", "admin/list-student");
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/get-student-by-id/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        try {
            Student studentReturn = iStudentService.getStudentById(id);
            StudentDTO studentDTO = StudentMapper.mapFromEntityToDTO(studentReturn);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-quantity-student-chart")
    public ResponseEntity<?> getQuantityStudentChart() {
        List<Integer> listQuantityStudentChart = iStudentService.getListQuantityStudentChart();
        return new ResponseEntity<>(listQuantityStudentChart, HttpStatus.OK);
    }

    @GetMapping("/dash-board/list-student/search")
    public String searchStudent(Model model,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Student> studentPage = iStudentService.searchStudent(keyword, page);

        List<StudentDTO> studentDTOList = studentPage.stream().map(
                item  ->  StudentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            studentDTOList = iStudentService.sortStudent(studentDTOList, sort);
        }

        List<ClassDTO> classDTOList = iClassService.getAllClass().stream().map(
                item -> ClassMapper.mapClassToClassResponse(item)
        ).collect(Collectors.toList());

        List<DepartmentDTO> departmentDTOList = iDepartmentService.getAllDepartment().stream().map(
                item -> DepartmentMapper.mapDepartmentToDepartmentResponse(item)
        ).collect(Collectors.toList());

        long quantityStudent = iStudentService.getQuantityStudent();

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        if(studentDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        model.addAttribute("totalPage", studentPage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("listStudent", studentDTOList);
        model.addAttribute("listClass", classDTOList);
        model.addAttribute("listDepartment", departmentDTOList);
        model.addAttribute("active", 2);
        model.addAttribute("quantityStudent", quantityStudent);
        model.addAttribute("pageName", "admin/list-student");
        return "/admin/dash-board";
    }

    @PostMapping("/add-new-student")
    public String addNewStudent(@ModelAttribute StudentDTO studentDTO, @RequestParam("imageStudent") MultipartFile image) throws IOException {
        Student studentInput = StudentMapper.mapFromDTOToEntity(studentDTO);
        messageResponse = iStudentService.addNewStudent(studentInput, image);
        showToast = true;

        return "redirect:/dash-board/list-student";
    }

    @PostMapping("/update-student")
    public String updateStudent(@ModelAttribute StudentDTO studentDTO, @RequestParam("imageStudentUpdate") MultipartFile image) throws IOException {
        Student studentInput = StudentMapper.mapFromDTOToEntity(studentDTO);
        messageResponse = iStudentService.updateStudent(studentInput, image);
        showToast = true;
        return "redirect:/dash-board/list-student";
    }

    @PostMapping("/delete-student")
    public String deleteStudent(@ModelAttribute StudentDTO studentDTO) throws IOException{
        messageResponse = iStudentService.deleteStudent(studentDTO.getStudentId());
        showToast = true;
        return "redirect:/dash-board/list-student";
    }

//    Quyền student

    @GetMapping("/student/infor")
    public String studentInfor(Model model) {

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("studentLogin", iStudentService.getStudentByEmail(authen.getName()).get());
        else model.addAttribute("studentLogin", null);

        model.addAttribute("active", 1);
        model.addAttribute("pageName", "/student/infor-student");
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        showToast = false;
        return "/student/dash-board";
    }

    @GetMapping("/student/time-table")
    public String getTimetable(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {

        Student student;

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) {
            student = iStudentService.getStudentByEmail(authen.getName()).get();
            model.addAttribute("studentLogin", student);
        }
        else {
            student = null;
            model.addAttribute("studentLogin", student);
        }

        Page<TimeTable> timeTablePage = iTimetableService.findAllByStudentId(student.getStudentId(), page);

        List<TimetableDTO> timetableDTOList = timeTablePage.stream().map(
                item -> TimetableMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        model.addAttribute("active", 2);
        model.addAttribute("totalPage", timeTablePage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("listTimetable", timetableDTOList);
        model.addAttribute("pageName", "/student/time-table");
        return "/student/dash-board";
    }

    @GetMapping("/student/register-course")
    public String registerCourse(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {

        Page<Schedule> schedulePage = iScheduleService.getListScheduleAndPaginationForStudent(page);

        List<ScheduleDTO> scheduleDTOList = schedulePage.stream().map(
                item -> ScheduleMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("studentLogin", iStudentService.getStudentByEmail(authen.getName()).get());
        else model.addAttribute("studentLogin", null);

        model.addAttribute("active", 3);
        model.addAttribute("totalPage", schedulePage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("listSchedules", scheduleDTOList);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("pageName", "/student/register-course");
        showToast = false;
        return "/student/dash-board";
    }

    @GetMapping("/student/sudent-course-grade")
    public String pointStudent(Model model) throws Exception{

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();

        Student studentLogin = null;
        if(authen.isAuthenticated()) {
            studentLogin = iStudentService.getStudentByEmail(authen.getName()).get();
            model.addAttribute("studentLogin", studentLogin);
        }
        else throw new Exception("Tài khoản đăng nhập chưa được xác thưc");

        List<StudentSubjectDTO> studentSubjectDTOList = iStudentSubjectService.getAllByStudentId(studentLogin.getStudentId()).stream().map(
                item -> StudentSubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        model.addAttribute("listPoints", studentSubjectDTOList);
        model.addAttribute("active", 4);
        model.addAttribute("pageName", "/student/list-point");
        return "/student/dash-board";
    }

    @PostMapping("/student/update-student")
    public String updateStudent(@ModelAttribute StudentDTO studentDTO) {
        Student student = StudentMapper.mapFromDTOToEntity(studentDTO);
        messageResponse = iStudentService.updateStudent(student);
        showToast = true;
        return "redirect:/student/infor";
    }

    @PostMapping("/student/change-password")
    public String changePassword(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword
    ) {

        messageResponse = iStudentService.changePassword(studentId, currentPassword, newPassword, confirmPassword);
        showToast = true;

        return "redirect:/student/infor";
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public void setMessageResponse(String messageResponse) {
        this.messageResponse = messageResponse;
    }
}
