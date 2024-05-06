package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "verifycode")
public class VerifyCode {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "verifycodeid")
    private int verifyCodeId;

    @Column(name = "email")
    private String email;

    @Column(name = "confirmcode")
    private String confirmCode;

    @Column(name = "expiredtime")
    private LocalDateTime expiredTime;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;
}
