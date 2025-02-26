package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.UserBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserBaseRepository extends JpaRepository<UserBaseEntity, UUID> {
    UserBaseEntity findByEmail(String email);
}
