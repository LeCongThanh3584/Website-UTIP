package com.example.demo.Controllers;

import com.example.demo.DTOs.SemesterDTO;
import com.example.demo.DTOs.StudentDTO;
import com.example.demo.DTOs.StudentSubjectDTO;
import com.example.demo.DTOs.SubjectDTO;
import com.example.demo.Entities.StudentSubject;
import com.example.demo.Mapper.SemesterMapper;
import com.example.demo.Mapper.StudentMapper;
import com.example.demo.Mapper.StudentSubjectMapper;
import com.example.demo.Mapper.SubjectMapper;
import com.example.demo.Services.Semester.ISemesterService;
import com.example.demo.Services.Student.IStudentService;
import com.example.demo.Services.StudentSubject.IStudentSubjectService;
import com.example.demo.Services.User.IUserService;
import com.example.demo.Services.subject.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentSubjectController {

    private String messageResponse;

    private boolean showToast = false;

    @Autowired
    private LecturerController lecturerController;

    private final IStudentService iStudentService;

    private final ISubjectService iSubjectService;

    private final IStudentSubjectService iStudentSubjectService;
    private final IUserService iUserService;

    private final ISemesterService iSemesterService;

    public StudentSubjectController(IStudentService iStudentService,
                                    ISubjectService iSubjectService,
                                    IStudentSubjectService iStudentSubjectService,
                                    IUserService iUserService,
                                    ISemesterService iSemesterService)
    {
        this.iStudentService = iStudentService;
        this.iSubjectService = iSubjectService;
        this.iStudentSubjectService = iStudentSubjectService;
        this.iUserService = iUserService;
        this.iSemesterService = iSemesterService;
    }

    @GetMapping("/dash-board/list-point")
    public String getListPointPage(Model model,
                   @RequestParam(required = false) String sort,
                   @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<StudentSubject> studentSubjectPage = iStudentSubjectService.getAllPointAndPagination(page);

        List<StudentSubjectDTO> studentSubjectDTOList = studentSubjectPage.stream().map(
                item -> StudentSubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            studentSubjectDTOList = iStudentSubjectService.sortPoint(studentSubjectDTOList, sort);
        }

        List<StudentDTO> studentDTOList = iStudentService.getAllStudent().stream().map(
                item -> StudentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SemesterDTO> semesterDTOList = iSemesterService.getAllSemester().stream().map(
                item -> SemesterMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SubjectDTO> subjectDTOList = iSubjectService.getAllSubject().stream().map(
                item -> SubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(studentSubjectDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("sort", sort);
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", studentSubjectPage.getTotalPages());
        model.addAttribute("listStudentSubject", studentSubjectDTOList);
        model.addAttribute("listStudents", studentDTOList);
        model.addAttribute("listSubjects", subjectDTOList);
        model.addAttribute("listSemesters", semesterDTOList);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("pageName", "admin/list-point");
        model.addAttribute("active", 5);
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/dash-board/list-point/search")
    public String searchPoint(Model model,
                      @RequestParam(required = false) String keyword,
                      @RequestParam(required = false) String sort,
                      @RequestParam(required = false, defaultValue = "0") Integer page
    )
    {
        Page<StudentSubject> studentSubjectPage = iStudentSubjectService.searchPoint(keyword, page);

        List<StudentSubjectDTO> studentSubjectDTOList = studentSubjectPage.stream().map(
                item -> StudentSubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if (sort != null) {
            studentSubjectDTOList = iStudentSubjectService.sortPoint(studentSubjectDTOList, sort);
        }

        List<StudentDTO> studentDTOList = iStudentService.getAllStudent().stream().map(
                item -> StudentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SemesterDTO> semesterDTOList = iSemesterService.getAllSemester().stream().map(
                item -> SemesterMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SubjectDTO> subjectDTOList = iSubjectService.getAllSubject().stream().map(
                item -> SubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(studentSubjectDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", studentSubjectPage.getTotalPages());
        model.addAttribute("keyword",keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("listStudentSubject", studentSubjectDTOList);
        model.addAttribute("listStudents", studentDTOList);
        model.addAttribute("listSubjects", subjectDTOList);
        model.addAttribute("listSemesters", semesterDTOList);
        model.addAttribute("pageName", "admin/list-point");
        model.addAttribute("active", 5);
        return "/admin/dash-board";
    }


    @GetMapping("/get-point-by-id/{id}")
    public ResponseEntity<?> getPointById(@PathVariable Integer id) {
        try {
            StudentSubject studentSubjectReturn = iStudentSubjectService.getPointById(id);
            StudentSubjectDTO studentSubjectDTO = StudentSubjectMapper.mapFromEntityToDTO(studentSubjectReturn);
            return new ResponseEntity<>(studentSubjectDTO, HttpStatus.OK);
        }catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-new-point")
    public String addNewPoint(@ModelAttribute StudentSubjectDTO studentSubjectDTO) {
        StudentSubject studentSubjectInput = StudentSubjectMapper.mapFromDTOToEntity(studentSubjectDTO);
        messageResponse = iStudentSubjectService.addNewStudentSubject(studentSubjectInput);
        showToast = true;
        return "redirect:/dash-board/list-point";
    }

    @PostMapping("/update-point")
    public String updatePoint(@ModelAttribute StudentSubjectDTO studentSubjectDTO) {
        StudentSubject studentSubjectUpdate = StudentSubjectMapper.mapFromDTOToEntity(studentSubjectDTO);
        messageResponse = iStudentSubjectService.updateStudentSubject(studentSubjectUpdate);
        showToast = true;
        return "redirect:/dash-board/list-point";
    }

    @PostMapping("/lecturer/update-point")
    public String updatePointFromLecturer(@ModelAttribute StudentSubjectDTO studentSubjectDTO) {
        StudentSubject studentSubjectUpdate = StudentSubjectMapper.mapFromDTOToEntity(studentSubjectDTO);
        lecturerController.setMessageResponse(iStudentSubjectService.updateStudentSubject(studentSubjectUpdate));
        lecturerController.setShowToast(true);
        return "redirect:/lecturer/sudent-course-grade";
    }

    @PostMapping("/delete-point")
    public String deletePoint(@RequestParam("id") Integer id) {
        messageResponse = iStudentSubjectService.deleteStudentSubject(id);
        showToast = true;
        return "redirect:/dash-board/list-point";
    }
}
