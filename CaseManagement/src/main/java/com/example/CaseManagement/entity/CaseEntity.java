package com.example.CaseManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cases")
public class CaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    private String caseName;
    private String caseNumber;
    private String caseType;
    private String description;
    private String stationName;
    private String province;
    private String officerName;
    private String officerContact;
    private String occurrenceDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBaseEntity user;

    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private LawyerEntity assignedlawyer;

    @OneToMany(mappedBy = "relatedCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Manages serialization
    private List<DocumentEntity> documents = new ArrayList<>();

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "last_updated")
    private String lastUpdated;
}
