package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.EmailVerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationTokenEntity, Long> {
    Optional<EmailVerificationTokenEntity> findByEmailAndVerificationCodeAndUsedFalse(String email, String verificationCode);
    void deleteByEmail(String email);

}
