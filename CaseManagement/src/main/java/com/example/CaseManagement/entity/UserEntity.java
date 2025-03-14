package com.example.CaseManagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends UserBaseEntity {
    // No additional fields, so no additional getters and setters needed
    // All methods are inherited from UserBaseEntity
}