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
@Table(name = "student")
public class Student implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentid")
    private int studentId;

    @Column(name = "classid", insertable = false, updatable = false)
    private int classId;

    @Column(name = "departmentid", insertable = false, updatable = false)
    private int departmentId;

    @Column(name = "roleid", insertable = false, updatable = false)
    private int roleId;

    @Column(name = "studentname")
    private String studentName;

    @Column(name = "studentcode")
    private String studentCode;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;

    @Column(name = "statuslearn")
    private String statusLearn;

    @Column(name = "creditsowe")
    private Double creditsOwe;

    @Column(name = "creditsaccumulate")
    private Double creditsAccumulate;

    @Column(name = "cumulativegpa")
    private Double cumulativeGPA;

    @Column(name = "address")
    private String address;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "image")
    private String image;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "classid")
    @JsonIgnore
    private Class studentClass;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<StudentSubject> studentSubjectList;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    @JsonIgnore
    private Department department;

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
        return this.email;
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
