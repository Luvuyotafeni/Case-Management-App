package com.example.CaseManagement.entity;

import com.example.CaseManagement.enumaration.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_base")
public abstract class UserBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private String imageKitProfileId; // Added new field for ImageKit file ID
    private boolean isEmailVerified;
    private boolean twoStepVerification = false;

    @Enumerated(EnumType.STRING)
    private Role role;
}