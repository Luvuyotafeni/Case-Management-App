package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.AdminEntity;

import java.util.List;

public interface AdminService {
    AdminEntity getAdminById(Long adminId);
    AdminEntity updateAdmin(Long adminId, AdminEntity updatedAdmin);
    List<AdminEntity> getAllAdmin();
    void deleteAdmin(Long adminId);
}
