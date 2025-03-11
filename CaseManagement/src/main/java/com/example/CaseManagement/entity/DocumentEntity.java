package com.example.CaseManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    private String caseName;
    private String fileName;
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "case_id")
    @JsonIgnore // Prevent infinite recursion
    private CaseEntity relatedCase;
}
