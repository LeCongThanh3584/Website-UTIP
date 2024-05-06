package com.example.demo.Repository;

import com.example.demo.Entities.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    @Query("SELECT cl FROM Class cl WHERE cl.className LIKE %?1% OR cl.location LIKE %?1%")
    Page<Class> searchClass(String keyword, Pageable pageable);

}
