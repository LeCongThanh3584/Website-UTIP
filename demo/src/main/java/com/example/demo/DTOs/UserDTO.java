package com.example.demo.DTOs;

import com.example.demo.Entities.Role;
import com.example.demo.Entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class UserDTO {
    private int userId;

    private String email;

    private String fullName;

    private String password;

    private String image;

    private Boolean enable;

    private int roleId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate updateAt;

    private Role role;

}
