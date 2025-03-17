package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CredentialsEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    CredentialsEntity findByUser(UserBaseEntity user);
    Optional<CredentialsEntity> findByUserId(Long userId);
}
