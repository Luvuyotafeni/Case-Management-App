package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<CaseEntity, Long> {
    // Change this to match the actual ID property name in UserBaseEntity
    List<CaseEntity> findByUser_UserId(Long userId);
    List<CaseEntity> findByAssignedlawyer_LawyerId(Long LawyerId);// Assuming the ID field in UserBaseEntity is 'userId'
}