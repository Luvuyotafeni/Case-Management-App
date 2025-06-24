package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.LawyerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LawyerRepository extends JpaRepository<LawyerEntity, Long> {

    Optional<LawyerEntity> findByUser_UserId(Long userId);
}
