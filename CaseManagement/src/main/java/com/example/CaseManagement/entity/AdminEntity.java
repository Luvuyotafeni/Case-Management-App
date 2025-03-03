package com.example.CaseManagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
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


}