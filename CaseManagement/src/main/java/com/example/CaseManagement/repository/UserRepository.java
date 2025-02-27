package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
