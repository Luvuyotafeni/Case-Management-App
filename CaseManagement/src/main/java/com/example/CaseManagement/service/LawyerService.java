package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.LawyerEntity;

import java.util.List;

public interface LawyerService {

    LawyerEntity getLawyerById(Long lawyerId);

    LawyerEntity getLawyerByUserId(Long userId);

    List<LawyerEntity> getAllLawyers();

    LawyerEntity updateLawyer(Long lawyerId, LawyerEntity updatedLawyer);

    LawyerEntity updateLawyerByUserId(Long userId, LawyerEntity updatedLawyer);

    void deleteLawyer(Long lawyerId);

    void deleteLawyerByUserId(Long userId);
}