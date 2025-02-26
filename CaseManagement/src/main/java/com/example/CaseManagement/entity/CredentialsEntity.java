package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "credentials")
public class CredentialsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID credentialsId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private UserBaseEntity user;  // Changed to UserBaseEntity

    @Column(nullable = false)
    private String password;
}
