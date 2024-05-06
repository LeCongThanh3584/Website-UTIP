package com.example.demo.Controllers;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.User;
import com.example.demo.Mapper.RoleMapper;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Services.Class.IClassService;
import com.example.demo.Services.Department.IDepartmentService;
import com.example.demo.Services.Lecturer.ILecturerService;
import com.example.demo.Services.Role.IRoleService;
import com.example.demo.Services.Student.IStudentService;
import com.example.demo.Services.User.IUserService;
import com.example.demo.Services.subject.ISubjectService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private String messageResponse;
    private boolean showToast;

    @Autowired
    private HomeController homeController;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IRoleService iRoleService;

    @GetMapping("/dash-board/list-user")
    public String getListAccountPage(Model model,
                                     @RequestParam(required = false, defaultValue = "0") Integer page,
                                     @RequestParam(required = false) String sort)
    {

        User userLogin;

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) {
            userLogin = iUserService.getUserByEmail(authen.getName()).get();
            model.addAttribute("user", userLogin);
        }
        else {
            userLogin = null;
            model.addAttribute("user",null);
        }

        Page<User> userPage = iUserService.getAllUserNotUserLogin(page, userLogin.getEmail());

        List<UserDTO> userDTOList = userPage.stream().map(
                item -> UserMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            userDTOList = iUserService.sortUser(sort, userDTOList);
        }

        List<RoleDTO> roleDTOList = iRoleService.getRoleAdminAndRoleUser().stream().map(
                item -> RoleMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(userDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        model.addAttribute("listUsers", userDTOList);
        model.addAttribute("listRoles", roleDTOList);
        model.addAttribute("quantityUser", iUserService.countUser());
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("pageNumber", page);
        model.addAttribute("sort", sort);
        model.addAttribute("pageName", "admin/list-user");
        model.addAttribute("active", 9);
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/dash-board/list-user/search")
    public String getListUSerPageBySearch(Model model,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false) String sort)
    {

        User userLogin;

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) {
            userLogin = iUserService.getUserByEmail(authen.getName()).get();
            model.addAttribute("user", userLogin);
        }
        else {
            userLogin = null;
            model.addAttribute("user",null);
        }

        Page<User> userPage = iUserService.searchUserAndPagination(keyword, page);

        List<UserDTO> userDTOList = userPage.stream().map(
                item -> UserMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(sort != null) {
            userDTOList = iUserService.sortUser(sort, userDTOList);
        }

        List<RoleDTO> roleDTOList = iRoleService.getRoleAdminAndRoleUser().stream().map(
                item -> RoleMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        if(userDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        model.addAttribute("listUsers", userDTOList);
        model.addAttribute("listRoles", roleDTOList);
        model.addAttribute("quantityUser", iUserService.countUser());
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("pageName", "admin/list-user");
        model.addAttribute("active", 9);
        return "/admin/dash-board";
    }

    @GetMapping("/get-user-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User userReturn = iUserService.getUserById(id);
            UserDTO userDTOReturn = UserMapper.mapFromEntityToDTO(userReturn);
            return new ResponseEntity<>(userDTOReturn, HttpStatus.OK);

        }catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-new-user")
    public String addNewUser(@ModelAttribute UserDTO userDTO, @RequestParam("imageAddNew") MultipartFile image) throws IOException {
        User userAddnew = UserMapper.mapFromDTOToEntity(userDTO);
        messageResponse = iUserService.addNewUser(userAddnew, image);
        showToast = true;
        return "redirect:/dash-board/list-user";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute UserDTO userDTO, @RequestParam("imageUpdate") MultipartFile image) throws IOException {
        User userUpdate = UserMapper.mapFromDTOToEntity(userDTO);
        messageResponse = iUserService.updateUser(userUpdate, image);
        showToast = true;
        return "redirect:/dash-board/list-user";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") Integer userId) throws IOException {
        messageResponse = iUserService.deleteUser(userId);
        showToast = true;
        return "redirect:/dash-board/list-user";
    }

    @PostMapping("/change-password-admin")
    public String changePasswordAdmin(
                                 @RequestParam("userId") Integer userId,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword)
    {
        String response = iUserService.changePassword(userId, currentPassword, newPassword, confirmPassword);

        homeController.setMessageResponse(response);
        homeController.setShowToast(true);

        return "redirect:/dash-board/home";
    }

    @PostMapping("/change-password-user")
    public String changePasswordUser(
            @RequestParam("userId") Integer userId,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword)
    {
        String response = iUserService.changePassword(userId, currentPassword, newPassword, confirmPassword);
        homeController.setMessageResponse(response);
        homeController.setShowToast(true);
        return "redirect:/user/home";
    }

    @PostMapping("/update-user-admin")  //Cập nhật thông tin của admin khi đăng nhập
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String updateInforADMIN(
                             @ModelAttribute UserDTO userDTO,
                             @RequestParam("imageUser") MultipartFile image
    ) throws IOException {
        User userInput = UserMapper.mapFromDTOToEntity(userDTO);
        String messageResponse = iUserService.updateUserLogin(userInput, image);

        homeController.setMessageResponse(messageResponse);
        homeController.setShowToast(true);

        return "redirect:/dash-board/home";
    }

    @PostMapping("/update-user-user")  //Cập nhật thông tin của người dùng khi đăng nhập
    @PreAuthorize("hasAnyRole('USER')")
    public String updateInforUser(
            @ModelAttribute UserDTO userDTO,
            @RequestParam("imageUser") MultipartFile image
    ) throws IOException {
        User userInput = UserMapper.mapFromDTOToEntity(userDTO);
        String messageResponse = iUserService.updateUserLogin(userInput, image);

        homeController.setMessageResponse(messageResponse);
        homeController.setShowToast(true);

        return "redirect:/user/home";
    }
}
