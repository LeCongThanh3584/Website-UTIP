package com.example.demo.Services.subject;

import com.example.demo.DTOs.SubjectDTO;
import com.example.demo.Entities.Department;
import com.example.demo.Entities.Lecturer;
import com.example.demo.Entities.Subject;
import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.LecturerRepository;
import com.example.demo.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.util.*;

@Service
public class SubjectService implements ISubjectService{

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Subject> getAllSubject() {
        return subjectRepository.findAll();
    }

    @Override
    public Page<Subject> getAllSubjectAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return subjectRepository.findAll(pageable);
    }

    @Override
    public Subject getSubjectById(Integer id) throws Exception {
        Optional<Subject> subjectOptional = subjectRepository.findById(id);
        if (subjectOptional.isEmpty()) throw new Exception("Học phần không tồn tại");

        return subjectOptional.get();
    }

    @Override
    public Page<Subject> searchSubject(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return subjectRepository.searchSubject(keyword, pageable);
    }

    @Override
    public long getQuantitySubject() {
        return subjectRepository.count();
    }

    @Override
    public List<SubjectDTO> sortSubject(List<SubjectDTO> subjectDTOList, String sort) {
        switch (sort) {
            case "subjectName":
                Collections.sort(subjectDTOList, new Comparator<SubjectDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(SubjectDTO o1, SubjectDTO o2) {
                        return collator.compare(o1.getSubjectName(), o2.getSubjectName());
                    }
                });
                break;
            case "courseCredits":
                Collections.sort(subjectDTOList, new Comparator<SubjectDTO>() {
                    @Override
                    public int compare(SubjectDTO o1, SubjectDTO o2) {
                        if(o1.getCourseCredits() < o2.getCourseCredits()) return -1;
                        else if(o1.getCourseCredits() > o2.getCourseCredits()) return 1;
                        else return 0;
                    }
                });
                break;
            case "lecturer":
                Collections.sort(subjectDTOList, new Comparator<SubjectDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(SubjectDTO o1, SubjectDTO o2) {
                        String fullNameLecturer_1 = o1.getLecturer().getLecturerName();
                        String fullNameLecturer_2 = o2.getLecturer().getLecturerName();

                        String name_1 = fullNameLecturer_1.substring(fullNameLecturer_1.lastIndexOf(" ") + 1);
                        String name_2 = fullNameLecturer_2.substring(fullNameLecturer_2.lastIndexOf(" ") + 1);
                        return collator.compare(name_1, name_2);
                    }
                });
                break;
        }
        return subjectDTOList;
    }

    @Override
    public String addNewSubject(Subject subject) {
        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(subject.getLecturerId());
        if(lecturerOptional.isEmpty()) return "Giảng viên có id " + subject.getSubjectId() + " không tồn tại";

        Optional<Department> departmentOptional = departmentRepository.findById(subject.getDepartmentId());
        if (departmentOptional.isEmpty()) return "Khoa/Viện có id " +  subject.getDepartmentId() + " không tồn tại";

        subject.setCreateAt(LocalDate.now());
        subject.setUpdateAt(LocalDate.now());
        subject.setLecturer(lecturerOptional.get());
        subject.setDepartment(departmentOptional.get());

        subjectRepository.save(subject);
        return "Thêm mới học phần thành công";
    }

    @Override
    public String updateSubject(Subject subject) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subject.getSubjectId());
        if(subjectOptional.isEmpty()) return "Môn học có id " + subject.getSubjectId() + " không tồn tại";

        Optional<Lecturer> lecturerOptional = lecturerRepository.findById(subject.getLecturerId());
        if(lecturerOptional.isEmpty()) return "Giảng viên có id " + subject.getLecturerId() + " không tồn tại";

        Optional<Department> departmentOptional = departmentRepository.findById(subject.getDepartmentId());
        if(departmentOptional.isEmpty()) return "Khoa/Viện có id " + subject.getDepartmentId() + " không tồn tại";

        subject.setCreateAt(subjectOptional.get().getCreateAt());
        subject.setUpdateAt(LocalDate.now());
        subject.setLecturer(lecturerOptional.get());
        subject.setDepartment(departmentOptional.get());

        subjectRepository.save(subject);

        return "Cập nhật học phần có id " + subject.getSubjectId() + " thành công";
    }

    @Override
    public String deleteSubject(Integer id) {
        Optional<Subject> subjectOptional = subjectRepository.findById(id);
        if (subjectOptional.isEmpty()) return "Môn học có id " + id + " không tồn tại";

        subjectRepository.deleteById(id);
        return "Xoá học phần có id " + id + " thành công";
    }
}
