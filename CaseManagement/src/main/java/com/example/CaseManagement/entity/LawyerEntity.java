package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "lawyers")
public class LawyerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lawyerId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserBaseEntity user;

    private String specialization;

    
}