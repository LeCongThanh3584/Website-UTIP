package com.example.demo.Services.StudentSubject;

import com.example.demo.DTOs.StudentSubjectDTO;
import com.example.demo.Entities.Semester;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.StudentSubject;
import com.example.demo.Entities.Subject;
import com.example.demo.Repository.SemesterRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Repository.StudentSubjectRepository;
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
public class StudentSubjectService implements IStudentSubjectService {

    @Autowired
    private StudentSubjectRepository studentSubjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public List<StudentSubject> getAllStudentSubject() {
        return studentSubjectRepository.findAll();
    }

    @Override
    public Page<StudentSubject> getPointByLecturerId(Integer lecturerId, Integer page) {
        Pageable pageable = PageRequest.of(page, 15);
        return studentSubjectRepository.getPointByLecturerId(lecturerId, pageable);
    }

    @Override
    public List<StudentSubject> getAllByStudentId(Integer studentId) {
        return studentSubjectRepository.findAllByStudentIdOrderByCreateAtDesc(studentId);
    }

    @Override
    public Page<StudentSubject> getAllPointAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return studentSubjectRepository.findAll(pageable);
    }

    @Override
    public Page<StudentSubject> searchPoint(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return studentSubjectRepository.searchPoint(keyword, pageable);
    }

    @Override
    public StudentSubject getPointById(Integer id) throws Exception {
        Optional<StudentSubject> studentSubjectOptional = studentSubjectRepository.findById(id);
        if(studentSubjectOptional.isEmpty()) throw new Exception("Điểm không tồn tại");

        return studentSubjectOptional.get();
    }

    @Override
    public List<StudentSubjectDTO> sortPoint(List<StudentSubjectDTO> studentSubjectDTOList, String sort) {
        switch (sort) {
            case "studentName":
                Collections.sort(studentSubjectDTOList, new Comparator<StudentSubjectDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(StudentSubjectDTO o1, StudentSubjectDTO o2) {
                        String fullName_1 = o1.getStudent().getStudentName();
                        String fullName_2 = o2.getStudent().getStudentName();

                        String name_1 = fullName_1.substring(fullName_1.lastIndexOf(" ") + 1);
                        String name_2 = fullName_2.substring(fullName_2.lastIndexOf(" ") + 1);

                        return collator.compare(name_1, name_2);
                    }
                });
                break;
            case "subjectName":
                Collections.sort(studentSubjectDTOList, new Comparator<StudentSubjectDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(StudentSubjectDTO o1, StudentSubjectDTO o2) {
                        return collator.compare(o1.getSubject().getSubjectName(), o2.getSubject().getSubjectName());
                    }
                });
                break;
            case "pointLetter":
                Collections.sort(studentSubjectDTOList, new Comparator<StudentSubjectDTO>() {
                    @Override
                    public int compare(StudentSubjectDTO o1, StudentSubjectDTO o2) {
                        return o1.getPointLetter().compareTo(o2.getPointLetter());
                    }
                });
                break;
        }
        return studentSubjectDTOList;
    }

    @Override
    public String addNewStudentSubject(StudentSubject studentSubject) {
        Optional<Student> studentOptional = studentRepository.findById(studentSubject.getStudentId());
        if(studentOptional.isEmpty()) return "Sinh viên có id " + studentSubject.getStudentId() + " không tồn tại";

        Optional<Subject> subjectOptional = subjectRepository.findById(studentSubject.getSubjectId());
        if(subjectOptional.isEmpty()) return "Học phần có id " + studentSubject.getSubjectId() + " không tồn tại";

        Optional<Semester> semesterOptional = semesterRepository.findById(studentSubject.getSemesterId());
        if(semesterOptional.isEmpty()) return "Học kì có id " + studentSubject.getSemesterId() + " không tồn tại";

        studentSubject.setCreateAt(LocalDate.now());
        studentSubject.setUpdateAt(LocalDate.now());
        studentSubject.setStudent(studentOptional.get());
        studentSubject.setSubject(subjectOptional.get());
        studentSubject.setSemester(semesterOptional.get());
        studentSubject.setPointLetter(calculatePointLetter(studentSubject.getPointProcess(), studentSubject.getPointFinal()));

        studentSubjectRepository.save(studentSubject);

        //Cập nhật lại số tín chỉ nợ, tín chỉ tích luỹ và CPA cho sinh viên
        if(studentSubject.getPointProcess() != null && studentSubject.getPointFinal() != null) {  //Có điểm gk và điểm ck
            updateLearnInforStudent(studentOptional.get());
        }

        return "Thêm mới điểm cho sinh viên " + studentOptional.get().getStudentName() + " thành công";
    }

    @Override
    public String updateStudentSubject(StudentSubject studentSubject) {
        Optional<StudentSubject> studentSubjectOptional = studentSubjectRepository.findById(studentSubject.getId());
        if (studentSubjectOptional.isEmpty()) return "Điểm không tồn tại để cập nhật, vui lòng thử lại";

        Optional<Student> studentOptional = studentRepository.findById(studentSubject.getStudentId());
        if(studentOptional.isEmpty()) return "Sinh viên có id " + studentSubject.getStudentId() + " không tồn tại";

        Optional<Subject> subjectOptional = subjectRepository.findById(studentSubject.getSubjectId());
        if(subjectOptional.isEmpty()) return "Môn học có id " + studentSubject.getSubjectId() + " không tồn tại";

        Optional<Semester> semesterOptional = semesterRepository.findById(studentSubject.getSemesterId());
        if(semesterOptional.isEmpty()) return "Học kì không tồn tại";

        studentSubject.setCreateAt(studentSubjectOptional.get().getCreateAt());
        studentSubject.setUpdateAt(LocalDate.now());
        studentSubject.setStudent(studentOptional.get());
        studentSubject.setSubject(subjectOptional.get());
        studentSubject.setSemester(semesterOptional.get());
        studentSubject.setPointLetter(calculatePointLetter(studentSubject.getPointProcess(), studentSubject.getPointFinal()));

        studentSubjectRepository.save(studentSubject);

        if(studentSubject.getPointProcess() != null && studentSubject.getPointFinal() != null) {  //Có điểm gk và điểm ck
            updateLearnInforStudent(studentOptional.get());
        }

        return "Cập nhật điểm cho sinh viên " + studentOptional.get().getStudentName() + " với mã lớp " + studentSubject.getClassCode() + " thành công";
    }

    @Override
    public String deleteStudentSubject(Integer id) {
        Optional<StudentSubject> studentSubjectOptional = studentSubjectRepository.findById(id);
        if(studentSubjectOptional.isEmpty()) return "Điểm không tồn tại, vui lòng thử lại";

        Student student = studentSubjectOptional.get().getStudent();
        String tenMonHoc = studentSubjectOptional.get().getSubject().getSubjectName();
        String tenSinhVien = student.getStudentName();

//        studentSubjectRepository.delete(studentSubjectOptional.get());

        updateLearnInforStudent(student); //Sau khi xoá môn học thì cập nhật lại thông tin học tập cho sinh viên

        return "Xoá điểm môn học " + tenMonHoc + " của sinh viên " + tenSinhVien + " thành công";
    }

    private double calculateAvgPoint(Double pointProcess, Double pointFinal) {
        return Math.round((pointProcess * 0.3 + pointFinal * 0.7) * 100.0) / 100.0; //Làm tròn đến 2 chữ số sau dấu phẩy
    }

    private String calculatePointLetter(Double pointProcess, Double pointFinal) {

        if(pointProcess == null || pointFinal == null) return "";

        if(pointProcess < 3 || pointFinal < 3) return "F"; //Điểm gk và điểm ck dưới 3 là trượt môn

        double point = calculateAvgPoint(pointProcess, pointFinal);

        if(point >= 9.5 && point <= 10) return "A+";
        else if (point >= 8.5 && point < 9.5) return "A";
        else if (point >= 8.0 && point < 8.5) return "B+";
        else if (point >= 7.0 && point < 8) return "B";
        else if (point >= 6.5 && point < 7) return "C+";
        else if (point >= 5.5 && point < 6.5) return "C";
        else if (point >= 5.0 && point < 5.5) return "D+";
        else if (point >= 4.0 && point < 5.0) return "D";
        else return "F";
    }

    private void updateLearnInforStudent(Student student) {

        //Lấy tất cả các môn học của sinh viên ra
        List<StudentSubject> studentSubjectList = studentSubjectRepository.findAllByStudentId(student.getStudentId());

        double creditsOwe = 0.0; //Số tín chỉ nợ
        double creditsAccumulate = 0.0; //Số tín chỉ tích luỹ
        double avgPointMulCredits = 0.0; //Phép nhân giữa điểm avg và số tín chỉ của môn học
        double avgPoint = 0.0;

        for (StudentSubject item : studentSubjectList) {
            if(item.getPointProcess() == null || item.getPointFinal() == null) continue; //nếu môn học nào chưa có điểm gk và ck thì bỏ qua

            avgPoint = calculateAvgPoint(item.getPointProcess(), item.getPointFinal());

            if(avgPoint < 4 || item.getPointProcess() < 3 || item.getPointFinal() < 3) { //tạch
                creditsOwe += item.getSubject().getCourseCredits(); //Cập nhật số tín chỉ nợ
                avgPointMulCredits += 0 * item.getSubject().getCourseCredits();
            } else { // không tạch
                creditsAccumulate += item.getSubject().getCourseCredits(); //Cập nhật số tín chỉ tích luỹ
                avgPointMulCredits += avgPoint * item.getSubject().getCourseCredits();
            }
        }

        double CPA = Math.round(avgPointMulCredits / (creditsOwe + creditsAccumulate) * 100.0) / 100.0; //Làm tròn đến 2 chữ số thập phân

        student.setCreditsOwe(creditsOwe);
        student.setCreditsAccumulate(creditsAccumulate);
        student.setCumulativeGPA(CPA);

        studentRepository.save(student);
    }
}
