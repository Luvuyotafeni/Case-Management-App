package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.*;
import com.example.CaseManagement.enumaration.Role;
import com.example.CaseManagement.repository.*;
import com.example.CaseManagement.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserEntity createUser(String name, String email, String phone, String profilePictureUrl, String password) {
        if(password == null || password.isEmpty()){
            throw new RuntimeException("password cannot be null");
        }

        UserEntity user = new UserEntity();
        // Access methods from parent class
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setRole(Role.USER);
        userRepository.save(user);

        createCredentials(user, password);
        return user;
    }



    @Override
    @Transactional
    public LawyerEntity createLawyer(String name, String email, String phone, String profilePictureUrl, String specialization, String password) {
        if(password == null || password.isEmpty()){
            throw new RuntimeException("password cannot be null");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setRole(Role.LAWYER);
        userRepository.save(user);
        createCredentials(user, password);

        LawyerEntity lawyer = new LawyerEntity();
        lawyer.setUser(user);
        lawyer.setSpecialization(specialization);
        lawyerRepository.save(lawyer);

        return lawyer;

    }

    @Override
    public AdminEntity createAdmin(String name, String email, String phone, String profilePictureUrl, String adminSpecificColumn, String password) {
        if(password == null || password.isEmpty()){
            throw new RuntimeException("password cannot be null");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        createCredentials(user, password);

        AdminEntity admin = new AdminEntity();
        admin.setUser(user);
        admin.setAdminSpecificColumn(adminSpecificColumn);
        adminRepository.save(admin);

        return admin;
    }

    @Override
    public UserBaseEntity authenticateUser(String email, String password) {
        UserBaseEntity user = userBaseRepository.findByEmail(email);
        if (user == null){
            throw new RuntimeException("User not found");
        }

        CredentialsEntity credentials = credentialsRepository.findByUser(user);
        if (credentials == null){
            throw new RuntimeException("creadentials not found");
        }

        if (!credentials.getPassword().equals(password)){
            throw new RuntimeException("Invalid password");
        }
        return user;
    }


    private CredentialsEntity createCredentials(UserBaseEntity user, String password){
        CredentialsEntity credentials = new CredentialsEntity();
        credentials.setUser(user);
        credentials.setPassword(password);
        credentialsRepository.save(credentials);
        return credentials;
    }
}
