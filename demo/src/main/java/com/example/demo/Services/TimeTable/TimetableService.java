package com.example.demo.Services.TimeTable;

import com.example.demo.Entities.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TimetableService implements ITimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StudentSubjectRepository studentSubjectRepository;

    @Override
    public Page<TimeTable> findAllByStudentId(Integer studentId, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return timetableRepository.findAllByStudentIdOrderByCreateAtDesc(studentId, pageable);
    }

    @Override
    public String addNewTimeTable(String classCode, Integer studentId)  {
        Optional<Schedule> scheduleOptional = scheduleRepository.findByClassCode(classCode);
        if(scheduleOptional.isEmpty()) return "Mã lớp học không tồn tại";

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if(studentOptional.isEmpty()) return "Sinh viên không tồn tại";

        if(scheduleOptional.get().getRegisStartTime().isAfter(LocalDate.now())) return "Chưa đến thời gian đăng ký";

        if(scheduleOptional.get().getRegisEndTime().isBefore(LocalDate.now())) return "Thời gian đăng ký đã hết hạn";

        Optional<TimeTable> timeTableOptional = timetableRepository.findByStudentIdAndClassCode(studentId, classCode);
        if(timeTableOptional.isPresent()) return "Bạn đã đăng ký lớp học này rồi";

        if(scheduleOptional.get().getRegistered() >= scheduleOptional.get().getMaxRegister()) return "Lớp đã đầy, không thể đăng ký thêm";

        TimeTable timeTable = new TimeTable();
        timeTable.setStudent(studentOptional.get());
        timeTable.setSubject(scheduleOptional.get().getSubject());
        timeTable.setSemester(scheduleOptional.get().getSemester());
        timeTable.setSchoolDay(scheduleOptional.get().getSchoolDay());
        timeTable.setTimeLearn(scheduleOptional.get().getTimeLearn());
        timeTable.setClassCode(scheduleOptional.get().getClassCode());
        timeTable.setLocation(scheduleOptional.get().getLocation());
        timeTable.setCreateAt(LocalDate.now());
        timeTable.setUpdateAt(LocalDate.now());

        timetableRepository.save(timeTable);

        StudentSubject studentSubject = new StudentSubject();
        studentSubject.setStudent(studentOptional.get());
        studentSubject.setSubject(scheduleOptional.get().getSubject());
        studentSubject.setSemester(scheduleOptional.get().getSemester());
        studentSubject.setClassCode(scheduleOptional.get().getClassCode());
        studentSubject.setStatus("Đang cập nhật");
        studentSubject.setCreateAt(LocalDate.now());
        studentSubject.setUpdateAt(LocalDate.now());

        studentSubjectRepository.save(studentSubject);

        scheduleOptional.get().setRegistered(scheduleOptional.get().getRegistered() + 1); //Tăng sĩ số của lớp đăng ký lên 1
        scheduleRepository.save(scheduleOptional.get());

        return "Đăng ký thành công, xem kết quả ở thời khoá biểu";
    }

    @Override
    public String deleteTimetable(String classCode, Integer studentId) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findByClassCode(classCode);
        if (scheduleOptional.isEmpty()) return "Mã lớp học không tồn tại";

        if(scheduleOptional.get().getRegisStartTime().isAfter(LocalDate.now())) return "Chưa đến thời gian đăng ký, không thể huỷ";

        if(scheduleOptional.get().getRegisEndTime().isBefore(LocalDate.now())) return "Đã hết hạn thời gian đăng ký, không thể huỷ";

        Optional<TimeTable> timeTableOptional = timetableRepository.findByStudentIdAndClassCode(studentId, classCode);
        if(timeTableOptional.isEmpty()) return "Bạn chưa đăng ký mã lớp này, không thể huỷ đăng ký";

        Optional<StudentSubject> studentSubjectOptional = studentSubjectRepository.findByStudentIdAndClassCode(studentId, classCode);
        if(studentSubjectOptional.isEmpty()) return "Bạn chưa đăng ký mã lớp này, không thể huỷ đăng ký";

        timetableRepository.delete(timeTableOptional.get());

        studentSubjectRepository.delete(studentSubjectOptional.get());

        scheduleOptional.get().setRegistered(scheduleOptional.get().getRegistered() - 1); //Giảm số lượng đăng ký đi 1
        scheduleRepository.save(scheduleOptional.get());

        return "Huỷ đăng ký thành công, xem kết quả ở thời khoá biểu";
    }
}
