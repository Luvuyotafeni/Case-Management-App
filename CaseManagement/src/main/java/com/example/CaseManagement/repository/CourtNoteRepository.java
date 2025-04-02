package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.CourtNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtNoteRepository extends JpaRepository<CourtNoteEntity, Long> {

    List<CourtNoteEntity> findByRelatedCase_CaseId(Long caseId);
    List<CourtNoteEntity> findByCourtDate_CourtDateId(Long courtDateId);
    List<CourtNoteEntity> findByRelatedCase_Assignedlawyer_LawyerId(Long lawyerId);
    List<CourtNoteEntity> findByRelatedCase_User_UserId(Long userId);
}
