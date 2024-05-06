package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjectid")
    private int subjectId;

    @Column(name = "subjectname")
    private String subjectName;

    @Column(name = "subjectcode")
    private String subjectCode;

    @Column(name = "coursecredits")
    private double courseCredits;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "departmentid", insertable = false,  updatable = false)
    private int departmentId;

    @Column(name = "lecturerid", insertable = false, updatable = false)
    private int lecturerId;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "subject")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<StudentSubject> studentSubjectList;

    @OneToMany(mappedBy = "subject")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Schedule> scheduleList;

    @ManyToOne
    @JoinColumn(name = "lecturerid")
    @JsonIgnore
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    @JsonIgnore
    private Department department;
}
