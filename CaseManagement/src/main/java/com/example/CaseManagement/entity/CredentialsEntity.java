package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "credentials")
public class CredentialsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialsId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private UserBaseEntity user;

    @Column(nullable = false)
    private String password;

    // Getters
    public Long getCredentialsId() {
        return credentialsId;
    }

    public UserBaseEntity getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setCredentialsId(Long credentialsId) {
        this.credentialsId = credentialsId;
    }

    public void setUser(UserBaseEntity user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}