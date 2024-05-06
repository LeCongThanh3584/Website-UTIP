package com.example.demo.Services.User;

import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.User;
import com.example.demo.Repository.LecturerRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) return userOptional.get();  //Trả về lớp mà implement userDetails

        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        if(studentOptional.isPresent()) return studentOptional.get();

        Optional<Lecturer> lecturerOptional = lecturerRepository.findByEmail(email);
        if(lecturerOptional.isPresent()) return lecturerOptional.get();

        throw new UsernameNotFoundException("Tài khoản không tồn tại");
    }
}
