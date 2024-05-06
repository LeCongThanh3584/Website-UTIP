package com.example.demo.Services.Class;

import com.example.demo.DTOs.ClassDTO;
import com.example.demo.Entities.Class;
import com.example.demo.Repository.ClassRepository;
import com.example.demo.Services.Class.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.time.LocalDate;
import java.util.*;

@Service
public class ClassService implements IClassService {
    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Override
    public List<Class> getAllClass() {
        return classRepository.findAll();
    }


    @Override
    public Page<Class> getAllClassAndPagination(Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return classRepository.findAll(pageable);
    }

    @Override
    public Long countClass() {
        return classRepository.count();
    }

    @Override
    public String addNewClass(Class newClass) {
        newClass.setSize(0);
        newClass.setCreateAt(LocalDate.now());
        newClass.setUpdateAt(LocalDate.now());

        classRepository.save(newClass);
        return "Thêm mới lớp học thành công";
    }

    @Override
    public String updateClass(Class updateClass) {
        Optional<Class> classOptional = classRepository.findById(updateClass.getClassId());
        if(classOptional.isEmpty()) return "Lớp có id: " + updateClass.getClassId() + " không tồn tại";

        updateClass.setCreateAt(classOptional.get().getCreateAt()); //Thời gian tạo mới lớp luôn phải được giữ nguyên
        updateClass.setUpdateAt(LocalDate.now());  //Thời gian cập nhật lớp phải là thời gian hiện tại

        classRepository.save(updateClass);
        return "Cập nhật thành công";
    }

    @Override
    public Class getClassById(Integer id) throws Exception {
        Optional<Class> classOptional = classRepository.findById(id);
        if(classOptional.isEmpty()) throw new Exception("Lớp không tồn tại");

        return classOptional.get();
    }

    @Override
    public Page<Class> searchClass(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return classRepository.searchClass(keyword, pageable);
    }

    @Override
    public List<ClassDTO> sortClass(List<ClassDTO> classDTOList, String sort) {
        switch (sort) {
            case "className":
                Collections.sort(classDTOList, new Comparator<ClassDTO>() {
                    Collator collator = Collator.getInstance(new Locale("vi"));
                    @Override
                    public int compare(ClassDTO o1, ClassDTO o2) {
                        return collator.compare(o1.getClassName(), o2.getClassName());
                    }
                });
                break;
            case "size":
                Collections.sort(classDTOList, new Comparator<ClassDTO>() {
                    @Override
                    public int compare(ClassDTO o1, ClassDTO o2) {
                        return o1.getSize() - o2.getSize();
                    }
                });
                break;
        }
        return classDTOList;
    }

    @Override
    public String deleteClass(Integer id) {
        Optional<Class> classOptional = classRepository.findById(id);
        if(classOptional.isEmpty()) return "Lớp có id = " + id + " không tồn tại";

        classRepository.deleteById(id);
        return "Xoá lớp có id " + id + " thành công";
    }

    @Override
    public Page<Class> phanTrang(Pageable pageable) {
        return classRepository.findAll(pageable);
    }


}
