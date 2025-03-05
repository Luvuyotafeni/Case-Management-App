package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.LawyerEntity;

import java.util.List;

public interface LawyerService {

    LawyerEntity getLawyerById(Long lawyerId);
    List<LawyerEntity> getAllLawyers();
    LawyerEntity updateLawyer(Long lawyerId, LawyerEntity updatedLawyer);
    void deleteLawyer(Long lawyerId);
}
