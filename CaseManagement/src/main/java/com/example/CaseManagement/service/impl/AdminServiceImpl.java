package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.AdminEntity;
import com.example.CaseManagement.repository.AdminRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import com.example.CaseManagement.repository.UserRepository;
import com.example.CaseManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Override
    public AdminEntity getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(()-> new RuntimeException("Admin not found with Id" + adminId));
    }

    @Override
    public AdminEntity updateAdmin(Long adminId, AdminEntity updateAdmin) {
        return null;
    }

    @Override
    public List<AdminEntity> getAllAdmin() {
        try{
            return adminRepository.findAll();
        } catch (Exception e){
            throw new RuntimeException("Error fetching all admins: " + e.getMessage());
        }
    }

    @Override
    public void deleteAdmin(Long adminId) {
        try {
            AdminEntity admin = getAdminById(adminId);
            userBaseRepository.delete(admin.getUser());
        } catch (Exception e){
            throw new RuntimeException("Error deleting admin: " + e.getMessage());
        }
    }

}
