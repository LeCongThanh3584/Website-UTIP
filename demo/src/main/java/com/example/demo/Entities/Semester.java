package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "semester")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semesterid")
    private int semesterId;

    @Column(name = "semestername")
    private String semesterName;

    @Column(name = "starttime")
    private LocalDate startTime;

    @Column(name = "endtime")
    private LocalDate endTime;

    @OneToMany(mappedBy = "semester")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private List<StudentSubject> studentSubjectList;

    @OneToMany(mappedBy = "semester")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "semester")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private List<TimeTable> timeTableList;
}
