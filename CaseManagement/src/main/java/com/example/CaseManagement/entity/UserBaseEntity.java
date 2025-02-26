package com.example.CaseManagement.entity;

import com.example.CaseManagement.enumaration.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_base")
public abstract class UserBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private boolean isEmailVerified;
    private boolean twoStepVerification = false;

    @Enumerated(EnumType.STRING)
    private Role role; // New field to track the role
}
