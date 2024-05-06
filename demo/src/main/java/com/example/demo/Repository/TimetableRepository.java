package com.example.demo.Repository;

import com.example.demo.Entities.TimeTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<TimeTable, Integer> {
    Page<TimeTable> findAllByStudentIdOrderByCreateAtDesc(Integer studentId, Pageable pageable);
    Optional<TimeTable> findByStudentIdAndClassCode(Integer studentId,String classCode);
}
