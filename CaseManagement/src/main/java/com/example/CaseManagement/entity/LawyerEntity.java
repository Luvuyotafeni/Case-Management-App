package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "lawyers")
public class LawyerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lawyerId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserBaseEntity user;

    private String specialization;

    // Getters
    public Long getLawyerId() {
        return lawyerId;
    }

    public UserBaseEntity getUser() {
        return user;
    }

    public String getSpecialization() {
        return specialization;
    }

    // Setters
    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public void setUser(UserBaseEntity user) {
        this.user = user;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}