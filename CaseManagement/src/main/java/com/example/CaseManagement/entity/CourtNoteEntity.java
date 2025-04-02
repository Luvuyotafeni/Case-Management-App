package com.example.CaseManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "court_notes")
public class CourtNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @ManyToOne
    @JoinColumn(name = "case_Id")
    @JsonBackReference
    private CaseEntity relatedCase;


    @ManyToOne
    @JoinColumn
    private  CourtDateEntity courtDate;

    @Column(length = 5000)
    private Long createdByUserId;

    @Column(name = "created_by_role")
    private String createdByRole;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "last_updated")
    private String lastUpdated;
}
