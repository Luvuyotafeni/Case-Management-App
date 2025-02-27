package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
}
