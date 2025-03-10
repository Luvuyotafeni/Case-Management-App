package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByRelatedCase_CaseId(Long caseId);
}
