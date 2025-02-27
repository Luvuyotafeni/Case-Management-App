package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "admins")
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  adminId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserBaseEntity user;

    private String adminSpecificColumn;

    // Getters
    public Long getAdminId() {
        return adminId;
    }

    public UserBaseEntity getUser() {
        return user;
    }

    public String getAdminSpecificColumn() {
        return adminSpecificColumn;
    }

    // Setters
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setUser(UserBaseEntity user) {
        this.user = user;
    }

    public void setAdminSpecificColumn(String adminSpecificColumn) {
        this.adminSpecificColumn = adminSpecificColumn;
    }
}