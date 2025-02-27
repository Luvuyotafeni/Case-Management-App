package com.example.CaseManagement.repository;

import com.example.CaseManagement.entity.LawyerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LawyerRepository extends JpaRepository<LawyerEntity, UUID> {
}
