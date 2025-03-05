package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.service.LawyerService;

import java.util.List;

public class LawyerServiceImpl implements LawyerService {
    @Override
    public LawyerEntity getLawyerById(Long userId) {
        return null;
    }

    @Override
    public List<LawyerEntity> getAllLawyers() {
        return List.of();
    }

    @Override
    public LawyerEntity updateLawyer(Long lawyerId, LawyerEntity updatedLawyer) {
        return null;
    }

    @Override
    public void deleteLawyer(Long lawyerId) {

    }
}
