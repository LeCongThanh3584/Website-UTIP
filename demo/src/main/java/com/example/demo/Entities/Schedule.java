package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleid")
    private int scheduleId;

    @Column(name = "subjectid", insertable = false, updatable = false)
    private int subjectId;

    @Column(name = "timelearn")
    private String timeLearn;

    @Column(name = "schoolday")
    private String schoolDay;

    @Column(name = "semesterid", insertable = false, updatable = false)
    private int semesterId;

    @Column(name = "maxregister")
    private int maxRegister;

    @Column(name = "registered")
    private int registered;

    @Column(name = "classCode", unique = true)
    private String classCode;

    @Column(name = "location")
    private String location;

    @Column(name = "regisstarttime")
    private LocalDate regisStartTime;

    @Column(name = "regisendtime")
    private LocalDate regisEndTime;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "subjectid")
    @JsonIgnore
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "semesterid")
    @JsonIgnore
    private Semester semester;
}
