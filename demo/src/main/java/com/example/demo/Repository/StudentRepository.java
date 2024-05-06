package com.example.demo.Repository;

import com.example.demo.Entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String email);
    @Query("SELECT sv FROM Student sv WHERE sv.studentCode LIKE %?1% " +
            "OR sv.studentName LIKE %?1% " +
            "OR sv.address LIKE %?1% " +
            "OR sv.department.departmentName LIKE %?1% " +
            "OR sv.studentClass.className LIKE %?1%")
    Page<Student> searchStudent(String keyword, Pageable pageable);

    @Query("SELECT COUNT(sv.studentId) FROM Student sv WHERE sv.gender = 'Nam'")
    Integer getQuantityStudentMale();

    @Query("SELECT COUNT(sv.studentId) FROM Student sv WHERE sv.gender = 'Nữ'")
    Integer getQuantityStudentFemale();

    @Query("SELECT COUNT(sv.studentId) FROM Student sv WHERE sv.statusLearn = 'Học'")
    Integer getQuantityStudentLearn();

    @Query("SELECT COUNT(sv.studentId) FROM Student sv WHERE sv.statusLearn = 'Buộc thôi học'")
    Integer getQuantityStudentOutLearn();

    @Query("SELECT COUNT(sv.studentId) FROM Student sv WHERE sv.statusLearn = 'Bảo lưu'")
    Integer getQuantityStudentReserve();
}
