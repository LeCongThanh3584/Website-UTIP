package com.example.demo.Repository;

import com.example.demo.Entities.Schedule;
import org.apache.catalina.util.Introspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Page<Schedule> findByOrderByCreateAtDesc(Pageable pageable);

    @Query("SELECT schedule FROM Schedule schedule WHERE schedule.subject.subjectCode LIKE %?1% " +
            "OR schedule.subject.subjectName LIKE %?1% " +
            "OR schedule.semester.semesterName LIKE %?1% " +
            "OR schedule.schoolDay LIKE %?1% " +
            "ORDER BY schedule.createAt DESC ")
    Page<Schedule> searchSchedule(String keyword, Pageable pageable);

    Optional<Schedule> findByClassCode(String classCode);
}
