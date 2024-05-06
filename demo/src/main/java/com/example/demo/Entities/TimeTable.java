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
@Table(name = "timetable")
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetableid")
    private int timeTableId;

    @Column(name = "studentid", insertable = false, updatable = false)
    private int studentId;

    @Column(name = "subjectid", insertable = false, updatable = false)
    private int subjectId;

    @Column(name = "timelearn")
    private String timeLearn;

    @Column(name = "schoolday")
    private String schoolDay;

    @Column(name = "classcode")
    private String classCode;

    @Column(name = "location")
    private String location;

    @Column(name = "semesterid", insertable = false, updatable = false)
    private int semesterId;

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
