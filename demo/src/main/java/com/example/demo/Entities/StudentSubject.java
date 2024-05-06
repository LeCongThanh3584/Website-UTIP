package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "student_subject")
public class StudentSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "studentid", insertable = false, updatable = false)
    private int studentId;

    @Column(name = "subjectid", insertable = false, updatable = false)
    private int subjectId;

    @Column(name = "semesterid", insertable = false, updatable = false)
    private int semesterId;

    @Column(name = "classcode")
    private String classCode;

    @Column(name = "pointprocess")
    private Double pointProcess;

    @Column(name = "pointfinal")
    private Double pointFinal;

    @Column(name = "pointletter")
    private String pointLetter;

    @Column(name = "status")
    private String status;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "studentid")
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subjectid")
    @JsonIgnore
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "semesterid")
    @JsonIgnore
    private Semester semester;
}
