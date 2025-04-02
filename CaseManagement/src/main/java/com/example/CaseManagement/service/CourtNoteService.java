package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.CourtNoteEntity;

import java.util.List;

public interface CourtNoteService {

    CourtNoteEntity createCourtNote(Long caseId, Long courtDateId, String noteContent,
                                    Long createdBy, String createdByRole);

    CourtNoteEntity updateCourtNote(Long noteId, String noteContent, Long updatedById);

    void deleteCourtNote(Long noteId, Long adminId);

    CourtNoteEntity getCourtNoteById(Long noteId);

    List<CourtNoteEntity> getCourtNotesByCaseId(Long caseId);

    List<CourtNoteEntity> getCourtNotesByCourtDateId(Long courtDateId);

    List<CourtNoteEntity> getCourtNotesByLawyerId(Long lawyerId);

    List<CourtNoteEntity> getCourtNotesByUserId(Long userId);
}
