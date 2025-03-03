package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.AdminEntity;
import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.entity.UserEntity;

public interface UserService {

    UserEntity createUser(String name, String email, String phone, String profilePictureUrl, String password);
    LawyerEntity createLawyer(String name, String email, String phone, String profilePictureUrl, String specialization,String password );
    AdminEntity createAdmin(String name, String email, String phone, String profilePictureUrl, String adminSpecificColumn,String password);

    //login
    UserBaseEntity authenticateUser(String email, String password);
    UserBaseEntity getUserByEmail(String email);
}
