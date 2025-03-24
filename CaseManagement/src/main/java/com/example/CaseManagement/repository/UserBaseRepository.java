package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.enumaration.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserBaseRepository extends JpaRepository<UserBaseEntity, Long> {
    UserBaseEntity findByEmail(String email);
    List<UserBaseEntity> findByRole(Role role);
}

