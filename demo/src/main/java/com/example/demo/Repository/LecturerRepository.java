package com.example.demo.Repository;

import com.example.demo.Entities.Lecturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    @Query("SELECT gv FROM Lecturer gv WHERE gv.lecturerName LIKE %?1% " +
            "OR gv.lecturerCode LIKE %?1% " +
            "OR gv.level LIKE %?1% " +
            "OR gv.department.departmentName LIKE %?1% " +
            "OR gv.phoneNumber LIKE %?1%")
    Page<Lecturer> searchLecturer(String keyword, Pageable pageable);

    @Query("SELECT COUNT(gv.lecturerId) FROM Lecturer gv WHERE gv.level = 'Thạc sĩ'")
    Integer getQuantityLecturerMaster();

    @Query("SELECT COUNT(gv.lecturerId) FROM Lecturer gv WHERE gv.level = 'Tiến sĩ'")
    Integer getQuantityLecturerDoctor();

    @Query("SELECT COUNT(gv.lecturerId) FROM Lecturer gv WHERE gv.level = 'Giáo sư'")
    Integer getQuantityLecturerProfessor();

    @Query("SELECT COUNT(gv.lecturerId) FROM Lecturer gv WHERE gv.level = 'Phó giáo sư'")
    Integer getQuantityLecturerAProfessor();

    Optional<Lecturer> findByEmail(String email);
}
