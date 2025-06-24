package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.repository.LawyerRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import com.example.CaseManagement.service.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LawyerServiceImpl implements LawyerService {

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Override
    public LawyerEntity getLawyerById(Long lawyerId) {
        return lawyerRepository.findById(lawyerId)
                .orElseThrow(()-> new RuntimeException("Lawyer not found with ID: " + lawyerId));
    }

    // New method to get lawyer by userId
    public LawyerEntity getLawyerByUserId(Long userId) {
        return lawyerRepository.findByUser_UserId(userId)
                .orElseThrow(()-> new RuntimeException("Lawyer not found for user ID: " + userId));
    }

    @Override
    public List<LawyerEntity> getAllLawyers() {
        return lawyerRepository.findAll();
    }

    @Override
    public LawyerEntity updateLawyer(Long lawyerId, LawyerEntity updatedLawyer) {
        LawyerEntity existingLawyer = getLawyerById(lawyerId);

        existingLawyer.setSpecialization(updatedLawyer.getSpecialization());

        UserBaseEntity user = existingLawyer.getUser();
        user.setName(updatedLawyer.getUser().getName());
        user.setEmail(updatedLawyer.getUser().getEmail());
        user.setPhone(updatedLawyer.getUser().getPhone());
        user.setProfilePictureUrl(updatedLawyer.getUser().getProfilePictureUrl());

        userBaseRepository.save(user);

        return lawyerRepository.save(existingLawyer);
    }

    // New method to update lawyer by userId
    public LawyerEntity updateLawyerByUserId(Long userId, LawyerEntity updatedLawyer) {
        LawyerEntity existingLawyer = getLawyerByUserId(userId);

        // Update lawyer-specific fields
        if (updatedLawyer.getSpecialization() != null) {
            existingLawyer.setSpecialization(updatedLawyer.getSpecialization());
        }

        // Update user base fields
        UserBaseEntity user = existingLawyer.getUser();
        if (updatedLawyer.getUser() != null) {
            UserBaseEntity updatedUser = updatedLawyer.getUser();

            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
        }

        userBaseRepository.save(user);
        return lawyerRepository.save(existingLawyer);
    }

    @Override
    public void deleteLawyer(Long lawyerId) {
        LawyerEntity lawyer = getLawyerById(lawyerId);
        lawyerRepository.delete(lawyer);
        userBaseRepository.delete(lawyer.getUser());
    }

    // New method to delete lawyer by userId
    public void deleteLawyerByUserId(Long userId) {
        LawyerEntity lawyer = getLawyerByUserId(userId);
        lawyerRepository.delete(lawyer);
        userBaseRepository.delete(lawyer.getUser());
    }
}