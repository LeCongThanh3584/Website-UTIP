package com.example.demo.Controllers;

import com.example.demo.DTOs.DepartmentDTO;
import com.example.demo.Entities.Department;
import com.example.demo.Mapper.DepartmentMapper;
import com.example.demo.Services.Department.IDepartmentService;
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

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DepartmentController {

    private boolean showToast = false;
    private String messageResponse = null;

    private final IDepartmentService iDepartmentService;
    private final IUserService iUserService;

    @Autowired
    public DepartmentController(IDepartmentService iDepartmentService, IUserService iUserService) {
        this.iDepartmentService = iDepartmentService;
        this.iUserService = iUserService;
    }

    @GetMapping("/dash-board/list-department")
    public String getListDepartmentPage(Model model,
                        @RequestParam(required = false) String sort,
                        @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Department> departmentPage = iDepartmentService.getAllDepartmentAndPagination(page);

        List<DepartmentDTO> departmentDTOList = departmentPage.stream().map(
                item -> DepartmentMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            departmentDTOList = iDepartmentService.sortDepartment(departmentDTOList, sort);
        }

        if(departmentDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", departmentPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("listDepartment", departmentDTOList);
        model.addAttribute("quantityDepartment", iDepartmentService.getQuantityDepartment());
        model.addAttribute("showToast", showToast);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("pageName", "admin/list-department");
        model.addAttribute("active", 7);
        showToast = false;

        return "/admin/dash-board";
    }

    @GetMapping("/get-department-by-id/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Integer id) {
        try {
            Department departmentReturn = iDepartmentService.getDepartmentById(id);
            DepartmentDTO departmentDTOReturn = DepartmentMapper.mapFromEntityToDTO(departmentReturn);
            return new ResponseEntity<>(departmentDTOReturn, HttpStatus.OK);
        }catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dash-board/list-department/search")
    public String getListDepartmentBySearch(
            Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

       Page<Department> departmentPage = iDepartmentService.getDepartmentBySearch(keyword, page);

       List<DepartmentDTO> departmentDTOList = departmentPage.stream().map(
               item -> DepartmentMapper.mapFromEntityToDTO(item)
       ).collect(Collectors.toList());

        if(sort != null) {
            departmentDTOList = iDepartmentService.sortDepartment(departmentDTOList, sort);
        }

        long quantityDepartment = iDepartmentService.getQuantityDepartment();

        if(departmentDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", departmentPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("quantityDepartment", quantityDepartment);
        model.addAttribute("listDepartment", departmentDTOList);
        model.addAttribute("active", 7);
        model.addAttribute("pageName", "admin/list-department");

        return "/admin/dash-board";
    }

    @PostMapping("/add-new-department")
    public String addNewDepartment(@ModelAttribute DepartmentDTO departmentDTO) {
        Department departmentInput = DepartmentMapper.mapFromDTOToEntity(departmentDTO);
        messageResponse = iDepartmentService.addNewDepartment(departmentInput);
        showToast = true;
        return "redirect:/dash-board/list-department";
    }

    @PostMapping("/update-department")
    public String updateDepartment(@ModelAttribute DepartmentDTO departmentDTO) {
        Department departmentInput = DepartmentMapper.mapFromDTOToEntity(departmentDTO);
        messageResponse = iDepartmentService.updateDepartment(departmentInput);
        showToast = true;

        return "redirect:/dash-board/list-department";
    }

    @PostMapping("/delete-department")
    public String deleteClass(@ModelAttribute DepartmentDTO departmentDTO) {
        messageResponse = iDepartmentService.deleteDepartment(departmentDTO.getDepartmentId());
        showToast = true;
        return "redirect:/dash-board/list-department";
    }

}
