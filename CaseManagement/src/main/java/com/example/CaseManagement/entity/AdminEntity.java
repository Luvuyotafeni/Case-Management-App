package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "admins")
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID adminId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserBaseEntity user;  // Changed to UserBaseEntity

    private String adminSpecificColumn;
}
