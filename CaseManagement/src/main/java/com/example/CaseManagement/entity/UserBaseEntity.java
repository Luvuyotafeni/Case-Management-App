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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private boolean isEmailVerified;
    private boolean twoStepVerification = false;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public boolean isTwoStepVerification() {
        return twoStepVerification;
    }

    public Role getRole() {
        return role;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public void setTwoStepVerification(boolean twoStepVerification) {
        this.twoStepVerification = twoStepVerification;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}