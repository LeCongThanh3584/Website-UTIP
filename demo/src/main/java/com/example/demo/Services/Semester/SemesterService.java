package com.example.demo.Services.Semester;

import com.example.demo.Entities.Semester;
import com.example.demo.Repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterService implements ISemesterService{

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public List<Semester> getAllSemester() {
        return semesterRepository.findAll();
    }
}
