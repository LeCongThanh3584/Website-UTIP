package com.example.demo.Controllers;

import com.example.demo.Entities.User;
import com.example.demo.Oauth2.CustomOauth2User;
import com.example.demo.Services.Class.IClassService;
import com.example.demo.Services.Department.IDepartmentService;
import com.example.demo.Services.Lecturer.ILecturerService;
import com.example.demo.Services.Student.IStudentService;
import com.example.demo.Services.User.IUserService;
import com.example.demo.Services.subject.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private boolean showToast = false;
    private String messageResponse = "";

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ILecturerService iLecturerService;

    @Autowired
    private IStudentService iStudentService;

    @Autowired
    private ISubjectService iSubjectService;

    @Autowired
    private IClassService iClassService;

    @Autowired
    private IDepartmentService iDepartmentService;

    @GetMapping("/dash-board/home")
    public String dashBoardHome(Model model) {

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        model.addAttribute("quantityClass", iClassService.countClass());
        model.addAttribute("quantityDepartment", iDepartmentService.getQuantityDepartment());
        model.addAttribute("quantityStudent", iStudentService.getQuantityStudent());
        model.addAttribute("quantityLecturer", iLecturerService.getQuantityLecturer());
        model.addAttribute("quantitySubject", iSubjectService.getQuantitySubject());

        model.addAttribute("showToast", showToast);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("active", 1);
        model.addAttribute("pageName", "/admin/home");
        model.addAttribute("quantityLevelLecturer", iLecturerService.getListQuantityLecturer());
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/user/home")
    public String getUserHome(Model model) {

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) {
            if(authen.getPrincipal() instanceof User) {  //Nếu người dùng đăng nhập bằng tài khoản đăng ký
                model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
            } else if(authen.getPrincipal() instanceof CustomOauth2User) { //Nếu người dùng đăng nhập bằng google
                CustomOauth2User customerOauth2User = (CustomOauth2User) authen.getPrincipal();
                User user = new User();
                user.setEmail(customerOauth2User.getEmail());
                user.setFullName(customerOauth2User.getName());
                user.setImage(customerOauth2User.getImage());
                model.addAttribute("user", user);
            }
        }
        else model.addAttribute("user",null);

        model.addAttribute("quantityClass", iClassService.countClass());
        model.addAttribute("quantityDepartment", iDepartmentService.getQuantityDepartment());
        model.addAttribute("quantityStudent", iStudentService.getQuantityStudent());
        model.addAttribute("quantityLecturer", iLecturerService.getQuantityLecturer());
        model.addAttribute("quantitySubject", iSubjectService.getQuantitySubject());

        model.addAttribute("showToast", showToast);
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("pageName", "/user/home");
        model.addAttribute("active", 1);
        showToast = false;
        return "/user/dash-board";
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public void setMessageResponse(String messageResponse) {
        this.messageResponse = messageResponse;
    }
}
