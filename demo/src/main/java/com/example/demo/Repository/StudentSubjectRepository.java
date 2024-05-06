package com.example.demo.Repository;

import com.example.demo.Entities.StudentSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Integer> {
    @Query("SELECT point FROM StudentSubject point WHERE point.student.studentName LIKE %?1% " +
            "OR point.subject.subjectName LIKE %?1%")
    Page<StudentSubject> searchPoint(String keyword, Pageable pageable);

    @Query("SELECT point FROM StudentSubject point " +
            "JOIN Subject hp ON point.subjectId = hp.subjectId WHERE hp.lecturerId = ?1 " +
            "ORDER BY point.createAt DESC ")
    Page<StudentSubject> getPointByLecturerId(Integer lecturerId, Pageable pageable);
    List<StudentSubject> findAllByStudentIdOrderByCreateAtDesc(Integer studentId);
    Optional<StudentSubject> findByStudentIdAndClassCode(Integer studentId, String classCode);
    List<StudentSubject> findAllByStudentId(Integer studentId);

}
