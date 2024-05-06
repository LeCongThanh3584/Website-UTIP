package com.example.demo.Controllers;

import com.example.demo.DTOs.DepartmentDTO;
import com.example.demo.DTOs.LecturerDTO;
import com.example.demo.DTOs.SubjectDTO;
import com.example.demo.Entities.Subject;
import com.example.demo.Mapper.DepartmentMapper;
import com.example.demo.Mapper.LecturerMapper;
import com.example.demo.Mapper.SubjectMapper;
import com.example.demo.Services.Department.IDepartmentService;
import com.example.demo.Services.Lecturer.ILecturerService;
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
public class SubjectController {

    private String messageResponse;

    private boolean showToast = false;

    private final ISubjectService iSubjectService;
    private final ILecturerService iLecturerService;
    private final IUserService iUserService;
    private final IDepartmentService iDepartmentService;

    public SubjectController(ISubjectService iSubjectService,
                             ILecturerService iLecturerService,
                             IUserService iUserService,
                             IDepartmentService iDepartmentService) {
        this.iSubjectService = iSubjectService;
        this.iLecturerService = iLecturerService;
        this.iUserService = iUserService;
        this.iDepartmentService = iDepartmentService;
    }

    @GetMapping("/dash-board/list-subject")
    public String getListSubjectPage(Model model,
                 @RequestParam(required = false) String sort,
                 @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Subject> subjectPage = iSubjectService.getAllSubjectAndPagination(page);

        List<SubjectDTO> subjectDTOList = subjectPage.stream().map(
                item -> SubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            subjectDTOList = iSubjectService.sortSubject(subjectDTOList, sort);
        }

        List<LecturerDTO> lecturerDTOList = iLecturerService.getAllLecturer().stream().map(
                item -> LecturerMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<DepartmentDTO> departmentDTOList = iDepartmentService.getAllDepartment().stream().map(
                item -> DepartmentMapper.mapDepartmentToDepartmentResponse(item)
        ).collect(Collectors.toList());

        if(subjectDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("totalPage", subjectPage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("listLecturers", lecturerDTOList);
        model.addAttribute("listDepartments", departmentDTOList);
        model.addAttribute("listSubjects", subjectDTOList);
        model.addAttribute("quantitySubject", iSubjectService.getQuantitySubject());
        model.addAttribute("sort", sort);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("active", 4);
        model.addAttribute("pageName", "admin/list-subject");
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/dash-board/list-subject/search")
    public String searchSubject(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Subject> subjectPage = iSubjectService.searchSubject(keyword, page);

        List<SubjectDTO> subjectDTOList = subjectPage.stream().map(
                item -> SubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            subjectDTOList = iSubjectService.sortSubject(subjectDTOList, sort);
        }

        List<LecturerDTO> lecturerDTOList = iLecturerService.getAllLecturer().stream().map(
                item -> LecturerMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<DepartmentDTO> departmentDTOList = iDepartmentService.getAllDepartment().stream().map(
                item -> DepartmentMapper.mapDepartmentToDepartmentResponse(item)
        ).collect(Collectors.toList());

        if (subjectDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", subjectPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("quantitySubject", iSubjectService.getQuantitySubject());
        model.addAttribute("listLecturers", lecturerDTOList);
        model.addAttribute("listDepartments", departmentDTOList);
        model.addAttribute("listSubjects", subjectDTOList);
        model.addAttribute("active", 4);
        model.addAttribute("pageName", "admin/list-subject");
        return "/admin/dash-board";
    }

    @GetMapping("/get-subject-by-id/{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Integer id) {
        try {
            Subject subjectReturn = iSubjectService.getSubjectById(id);
            SubjectDTO subjectDTOReturn = SubjectMapper.mapFromEntityToDTO(subjectReturn);
            return new ResponseEntity<>(subjectDTOReturn, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-new-subject")
    public String addNewSubject(@ModelAttribute SubjectDTO subjectDTO) {
        Subject subjectInput = SubjectMapper.mapFromDTOToEntity(subjectDTO);
        messageResponse = iSubjectService.addNewSubject(subjectInput);
        showToast = true;
        return "redirect:/dash-board/list-subject";
    }

    @PostMapping("/update-subject")
    public String updateSubject(@ModelAttribute SubjectDTO subjectDTO) {
        Subject subjectUpdate = SubjectMapper.mapFromDTOToEntity(subjectDTO);
        messageResponse = iSubjectService.updateSubject(subjectUpdate);
        showToast = true;
        return "redirect:/dash-board/list-subject";
    }

    @PostMapping("/delete-subject")
    public String deleteSubject(@ModelAttribute SubjectDTO subjectDTO) {
        messageResponse = iSubjectService.deleteSubject(subjectDTO.getSubjectId());
        showToast = true;
        return "redirect:/dash-board/list-subject";
    }
}
