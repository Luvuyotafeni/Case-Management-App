package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<CaseEntity, Long> {
    List<CaseEntity> findByUserId(Long UserId);
}
