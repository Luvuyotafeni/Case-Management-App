package com.example.CaseManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "court_dates")
public class CourtDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courtDateId;


    @ManyToOne
    @JoinColumn(name = "case_id")
    @JsonBackReference
    private CaseEntity relatedCase;

    private LocalDateTime scheduledDateTime;
    private String courtName;
    private String courtRoom;
    private String judgeAssigned;
    private String hearingType;
    private String status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "lastUpdated")
    private String lastUpdated;
}
