package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByEmailAndOtpAndUsedFalse(String email, String otp);
}
