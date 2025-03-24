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
                .orElseThrow(()-> new RuntimeException("Lawyer not found" + lawyerId));
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

    @Override
    public void deleteLawyer(Long lawyerId) {
        LawyerEntity lawyer = getLawyerById(lawyerId);
        lawyerRepository.delete(lawyer);
        userBaseRepository.delete(lawyer.getUser());
    }
}
