package com.example.demo.Controllers;

import com.example.demo.DTOs.ClassDTO;
import com.example.demo.Entities.Class;
import com.example.demo.Mapper.ClassMapper;
import com.example.demo.Services.Class.IClassService;
import com.example.demo.Services.User.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClassController {
    private final IClassService iClassService;
    private final IUserService iUserService;

    @Autowired
    public ClassController(IClassService iClassService, IUserService iUserService) {
        this.iClassService = iClassService;
        this.iUserService = iUserService;
    }

    private boolean showToast = false;
    private String messageResponse = "";

    @GetMapping("/dash-board/list-class")
    public String getListClassPage(Model model,
                                   @RequestParam(required = false) String sort,
                                   @RequestParam(required = false, defaultValue = "0") Integer page)
    {
        Page<Class> classPage = iClassService.getAllClassAndPagination(page);

        List<ClassDTO> classDTOList = classPage.stream().map(
                item -> ClassMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            classDTOList = iClassService.sortClass(classDTOList, sort);
        }

        if(classDTOList.isEmpty()) {  //Dùng để trả về  nội dụng khi dữ liệu không được tìm thấy
            model.addAttribute("noResultFindAll", true);
        }
        else model.addAttribute("noResultFindAll", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("sort", sort);
        model.addAttribute("totalPage", classPage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("active", 3);
        model.addAttribute("listClass", classDTOList);
        model.addAttribute("classDTO", new ClassDTO());
        model.addAttribute("quantityClass", iClassService.countClass());
        model.addAttribute("showToast", showToast);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("pageName", "admin/list-class");
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/dash-board/list-class/search")
    public String getListClassPageBySearch(
            Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Class> classPage = iClassService.searchClass(keyword, page);

        List<ClassDTO> classDTOList = classPage.stream().map(
                item -> ClassMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        Long quantityClass = iClassService.countClass();

        if(sort != null) {
            classDTOList = iClassService.sortClass(classDTOList, sort);
        }

        if(classDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("sort", sort);
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPage", classPage.getTotalPages());
        model.addAttribute("classDTO", new ClassDTO());
        model.addAttribute("active", 3);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listClass", classDTOList);
        model.addAttribute("quantityClass", quantityClass);
        model.addAttribute("pageName", "admin/list-class");
        return "/admin/dash-board";
    }

    @GetMapping("/get-class-by-id/{id}")
    public ResponseEntity<?> getClassById(@PathVariable Integer id) {
        try {
            Class classReturn = iClassService.getClassById(id);
            ClassDTO classDTOReturn = ClassMapper.mapFromEntityToDTO(classReturn);
            return new ResponseEntity<>(classDTOReturn, HttpStatus.OK);

        }catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/add-new-class")
    public String addNewClass(@ModelAttribute ClassDTO classDTO) {
        Class classInput = ClassMapper.mapFromDTOToEntity(classDTO);
        messageResponse = iClassService.addNewClass(classInput);
        showToast = true;
        return "redirect:/dash-board/list-class";
    }

    @PostMapping("/update-class")
    public String upDateClass(@ModelAttribute ClassDTO classDTO) {
        Class classUpdate = ClassMapper.mapFromDTOToEntity(classDTO);
        messageResponse = iClassService.updateClass(classUpdate);
        showToast = true;
        return "redirect:/dash-board/list-class";
    }

    @PostMapping("/delete-class")
    public String deleteClass(@ModelAttribute ClassDTO classDTO) {
        //xoá thì chỉ gửi lên mình classId thôi
        messageResponse = iClassService.deleteClass(classDTO.getClassId());
        showToast = true;
        return "redirect:/dash-board/list-class";
    }
}
