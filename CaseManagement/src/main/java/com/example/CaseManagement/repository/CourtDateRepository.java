package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CourtDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourtDateRepository extends JpaRepository<CourtDateEntity, Long> {

    List<CourtDateEntity> findByRelatedCase_CaseId(Long caseId);
    List<CourtDateEntity> findByScheduledDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<CourtDateEntity> findByRelatedCase_Assignedlawyer_LawyerId(Long lawyerId);
    List<CourtDateEntity> findByRelatedCase_User_UserId(Long userId);
}
