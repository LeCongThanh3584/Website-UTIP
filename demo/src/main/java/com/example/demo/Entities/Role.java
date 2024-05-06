package com.example.demo.Entities;

import com.example.demo.Enums.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleid")
    private int roleId;

    @Column(name = "rolename")
    @Enumerated(EnumType.STRING)
    private ERole roleName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<User> userList;

    @OneToMany(mappedBy = "role")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Student> studentList;

    @OneToMany(mappedBy = "role")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Lecturer> lecturerList;

}
