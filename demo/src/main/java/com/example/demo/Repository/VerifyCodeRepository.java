package com.example.demo.Repository;

import com.example.demo.Entities.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Integer> {
    Optional<VerifyCode> findByConfirmCode(String confirmCode);
}
