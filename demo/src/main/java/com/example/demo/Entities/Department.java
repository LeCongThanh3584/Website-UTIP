package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departmentid")
    private int departmentId;

    @Column(name = "departmentcode")
    private String departmentCode;

    @Column(name = "departmentname")
    private String departmentName;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "department")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Student> studentList;

    @OneToMany(mappedBy = "department")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Lecturer> lecturerList;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Subject> subjectList;
}
