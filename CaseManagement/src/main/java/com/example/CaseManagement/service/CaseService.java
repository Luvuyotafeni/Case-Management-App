package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.CaseEntity;
import com.example.CaseManagement.entity.DocumentEntity;
import java.util.List;

public interface CaseService {
    CaseEntity createCase(String caseName, String caseNumber, String caseType, String description,
                          String stationName, String province, String officerName, String officerContact,
                          String occurrenceDate, Long userId);

    CaseEntity assignLawyer(Long caseId, Long lawyerId);

    DocumentEntity addDocument(Long caseId, String fileName, String fileUrl, String fileId);

    List<CaseEntity> getCasesByUserId(Long userId);

    CaseEntity getCaseById(Long caseId);

    List<DocumentEntity> getDocumentsByCaseId(Long caseId);
    List<CaseEntity> getAllCases();

    List<CaseEntity> getCasesByLawyerId(Long lawyerId);
}
