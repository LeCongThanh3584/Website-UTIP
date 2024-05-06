package com.example.demo.Repository;

import com.example.demo.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT user FROM User user where user.email LIKE %?1% OR user.fullName LIKE %?1% ")
    Page<User> searchUserAndPagination(String keyword, Pageable pageable);

    Page<User> findAllByEmailNot(String email, Pageable pageable);
}
