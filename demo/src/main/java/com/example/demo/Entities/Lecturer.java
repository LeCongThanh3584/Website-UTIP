package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "lecturer")
public class Lecturer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecturerid")
    private int lecturerId;

    @Column(name = "lecturername")
    private String lecturerName;

    @Column(name = "lecturercode")
    private String lecturerCode;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "level")
    private String level;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "departmentid", insertable = false, updatable = false)
    private int departmentId;

    @Column(name = "roleid", insertable = false, updatable = false)
    private int roleId;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "lecturer")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Subject> subjectList;

    @ManyToOne
    @JoinColumn(name = "roleid")
    @JsonIgnore
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRoleName().name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
