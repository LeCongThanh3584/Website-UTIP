package com.example.demo.Services.Schedule;

import com.example.demo.Entities.Schedule;
import com.example.demo.Entities.Semester;
import com.example.demo.Entities.Subject;
import com.example.demo.Repository.ScheduleRepository;
import com.example.demo.Repository.SemesterRepository;
import com.example.demo.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService implements IScheduleService{

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public Page<Schedule> getListScheduleAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return scheduleRepository.findByOrderByCreateAtDesc(pageable);
    }

    @Override
    public Page<Schedule> getListScheduleAndPaginationForStudent(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return scheduleRepository.findByOrderByCreateAtDesc(pageable);
    }

    @Override
    public Page<Schedule> searchSchedule(String search, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return scheduleRepository.searchSchedule(search, pageable);
    }

    @Override
    public Schedule getScheduleById(Integer id) throws Exception {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if(scheduleOptional.isEmpty()) throw new Exception("Học phần không tồn tại");

        return scheduleOptional.get();
    }

    @Override
    public String addNewSchedule(Schedule schedule) {
        Optional<Subject> subjectOptional = subjectRepository.findById(schedule.getSubjectId());
        if(subjectOptional.isEmpty()) return "Học phần không tồn tại";

        Optional<Semester> semesterOptional = semesterRepository.findById(schedule.getSemesterId());
        if(semesterOptional.isEmpty()) return "Học kỳ không tồn tại";

        schedule.setRegistered(0);
        schedule.setCreateAt(LocalDate.now());
        schedule.setUpdateAt(LocalDate.now());
        schedule.setSubject(subjectOptional.get());
        schedule.setSemester(semesterOptional.get());

        scheduleRepository.save(schedule);

        return "Thêm mới lịch học thành công";
    }

    @Override
    public String updateSchedule(Schedule schedule) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(schedule.getScheduleId());
        if(scheduleOptional.isEmpty()) return "Lịch học có id = " + schedule.getScheduleId() + " không tồn tại";

        Optional<Subject> subjectOptional = subjectRepository.findById(schedule.getSubjectId());
        if(subjectOptional.isEmpty()) return "Học phần có id = " + schedule.getSubjectId() + " không tồn tại";

        Optional<Semester> semesterOptional = semesterRepository.findById(schedule.getSemesterId());
        if(semesterOptional.isEmpty()) return "Học kỳ không tồn tại";

        schedule.setRegistered(scheduleOptional.get().getRegistered());
        schedule.setSubject(subjectOptional.get());
        schedule.setCreateAt(scheduleOptional.get().getCreateAt());
        schedule.setUpdateAt(LocalDate.now());
        schedule.setSemester(semesterOptional.get());

        scheduleRepository.save(schedule);

        return "Cập nhật lịch học có mã học phần " + subjectOptional.get().getSubjectCode() + " thành công";
    }

    @Override
    public String deleteSchedule(Integer scheduleId) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);
        if(scheduleOptional.isEmpty()) return "Lịch học có id " + scheduleId + " không tồn tại";

        scheduleRepository.deleteById(scheduleId);
        return "Xoá lịch học có id " + scheduleId + " thành công";
    }
}
