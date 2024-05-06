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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classid")
    private int classId;

    @Column(name = "classname")
    private String className;

    @Column(name = "size")
    private int size;

    @Column(name = "location")
    private String location;

    @Column(name = "createat")
    private LocalDate createAt;

    @Column(name = "updateat")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "studentClass")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Student> studentList;
}
