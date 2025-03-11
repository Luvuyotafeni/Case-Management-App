
package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.CaseEntity;
import com.example.CaseManagement.entity.DocumentEntity;
import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.entity.UserEntity;
import com.example.CaseManagement.repository.CaseRepository;
import com.example.CaseManagement.repository.DocumentRepository;
import com.example.CaseManagement.repository.LawyerRepository;
import com.example.CaseManagement.repository.UserRepository;
import com.example.CaseManagement.service.CaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private DocumentRepository documentRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public CaseEntity createCase(String caseName, String caseNumber, String caseType, String description,
                                 String stationName, String province, String officerName, String officerContact,
                                 String occurrenceDate, Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        CaseEntity caseEntity = new CaseEntity();
        caseEntity.setCaseName(caseName);
        caseEntity.setCaseNumber(caseNumber);
        caseEntity.setCaseType(caseType);
        caseEntity.setDescription(description);
        caseEntity.setStationName(stationName);
        caseEntity.setProvince(province);
        caseEntity.setOfficerName(officerName);
        caseEntity.setOfficerContact(officerContact);
        caseEntity.setOccurrenceDate(occurrenceDate);
        caseEntity.setUser(user);

        String now = LocalDateTime.now().format(formatter);
        caseEntity.setCreationDate(now);
        caseEntity.setLastUpdated(now);

        return caseRepository.save(caseEntity);
    }

    @Override
    @Transactional
    public CaseEntity assignLawyer(Long caseId, Long lawyerId) {
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));

        LawyerEntity lawyer = lawyerRepository.findById(lawyerId)
                .orElseThrow(() -> new RuntimeException("Lawyer not found with ID: " + lawyerId));

        caseEntity.setAssignedlawyer(lawyer);
        caseEntity.setLastUpdated(LocalDateTime.now().format(formatter));

        return caseRepository.save(caseEntity);
    }

    @Override
    @Transactional
    public DocumentEntity addDocument(Long caseId, String fileName, String fileUrl, String fileId) {
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        DocumentEntity document = new DocumentEntity();
        document.setCaseName(caseEntity.getCaseName());
        document.setFileName(fileName);
        document.setFileUrl(fileUrl);
        document.setFileId(fileId); // Store fileId

        document.setRelatedCase(caseEntity);
        return documentRepository.save(document);
    }


    @Override
    public List<CaseEntity> getCasesByUserId(Long userId) {
        return caseRepository.findByUser_UserId(userId);
    }

    @Override
    public CaseEntity getCaseById(Long caseId) {
        return caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));
    }

    @Override
    public List<DocumentEntity> getDocumentsByCaseId(Long caseId) {
        return documentRepository.findByRelatedCase_CaseId(caseId);
    }
}