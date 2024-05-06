package com.example.demo.Repository;

import com.example.demo.Entities.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Query("SELECT hp FROM Subject hp WHERE hp.subjectName LIKE %?1% " +
            "OR hp.subjectCode LIKE %?1% " +
            "OR hp.department.departmentName LIKE %?1% " +
            "OR hp.lecturer.lecturerName LIKE %?1%")
    Page<Subject> searchSubject(String keyword, Pageable pageable);
}
