package com.example.demo.Controllers;

import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.User;
import com.example.demo.Services.Lecturer.ILecturerService;
import com.example.demo.Services.Student.IStudentService;
import com.example.demo.Services.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private String messageResponse;

    private boolean showToast;

    private final IUserService iUserService;
    private final IStudentService iStudentService;
    private final ILecturerService iLecturerService;

    @Autowired
    public AuthController(IUserService iUserService,
                          IStudentService iStudentService,
                          ILecturerService iLecturerService) {
        this.iUserService = iUserService;
        this.iStudentService = iStudentService;
        this.iLecturerService = iLecturerService;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/register";
    }

    @PostMapping("/register-account")
    public String registerAcc(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              @RequestParam("confirmPassword") String cfPassword)
    {
        messageResponse = iUserService.register(email, password, cfPassword);
        showToast = true;
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        showToast = false;
        return "/login";
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        model.addAttribute("messageResponse", messageResponse);
        model.addAttribute("showToast", showToast);
        showToast = false;
        return "/resetPassword";
    }

    @PostMapping("/forget-password") //Gửi mã xác thực về cho email
    public String forgetPassword(@RequestParam("email") String email) {
        boolean response = iUserService.forgetPassword(email);
        if(response) {
            return "redirect:/reset-password";
        } else {
            messageResponse = "Email không tồn tại";
            showToast = true;
            return "redirect:/login";
        }
    }

    @PostMapping("/reset-password") //Đặt lại mật khẩu
    public String resetPassword(@RequestParam String confirmCode,
                                @RequestParam String password)
    {
        boolean response = iUserService.resetPassword(confirmCode, password);
        if(response) {
            messageResponse = "Đặt lại mật khẩu thành công";
            showToast = true;
            return "redirect:/login";
        } else {
            messageResponse = "Mã xác nhận không đúng hoặc đã hết hạn, vui lòng thử lại";
            showToast = true;
            return "redirect:/reset-password";
        }

    }
}
