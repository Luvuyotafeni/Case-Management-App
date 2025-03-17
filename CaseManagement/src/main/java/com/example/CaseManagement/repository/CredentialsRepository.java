package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CredentialsEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    CredentialsEntity findByUser(UserBaseEntity user);

    // Change this method to use the correct property navigation
    Optional<CredentialsEntity> findByUser_UserId(Long userId);
}