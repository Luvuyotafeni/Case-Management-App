package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.AdminEntity;
import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.entity.UserEntity;
import com.example.CaseManagement.enumaration.Role;

import java.util.List;

public interface UserService {

    UserEntity createUser(String name, String email, String phone, String profilePictureUrl, String password);
    LawyerEntity createLawyer(String name, String email, String phone, String profilePictureUrl, String specialization,String password );
    AdminEntity createAdmin(String name, String email, String phone, String profilePictureUrl, String adminSpecificColumn,String password);

    UserBaseEntity getUserByEmail(String email);
    UserEntity updateUser(Long userId, UserEntity updatedUser);
    UserBaseEntity getUserBaseById(Long userId);
    List<UserBaseEntity> getUsersByRole(Role role);
}
