package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.*;
import com.example.CaseManagement.enumaration.Role;
import com.example.CaseManagement.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.CaseManagement.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        emailVerificationService.sendVerificationEmail(user);
        return user;
    }


    @Override
    @Transactional
    public LawyerEntity createLawyer(String name, String email, String phone, String profilePictureUrl, String specialization, String password) {
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Password cannot be null");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user has the 'ADMIN' role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Only admins can create lawyers");
        }

        // Create lawyer user
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setRole(Role.LAWYER);
        userRepository.save(user);
        createCredentials(user, password);

        emailVerificationService.sendVerificationEmail(user);

        // Create lawyer entity
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

        emailVerificationService.sendVerificationEmail(user);

        AdminEntity admin = new AdminEntity();
        admin.setUser(user);
        admin.setAdminSpecificColumn(adminSpecificColumn);
        adminRepository.save(admin);

        return admin;
    }

    @Override
    public UserBaseEntity getUserByEmail(String email) {
        UserBaseEntity user = userBaseRepository.findByEmail(email);
        if (user == null){
            throw new RuntimeException("User not found with email: "+ email);
        }
        return user;
    }

    @Override
    public UserEntity updateUser(Long userId, UserEntity updatedUser) {

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found: "+ userId));

        //get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserBaseEntity currentUser = userBaseRepository.findByEmail(authentication.getName());

        //checking id the current user is the same as the one being updated
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin  && !existingUser.getUserId().equals(currentUser.getUserId())){
            throw new RuntimeException("You can only update your own profile");
        }

        String oldEmail = existingUser.getEmail();

        if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhone() != null ) existingUser.setPhone(updatedUser.getPhone());
        if (updatedUser.getProfilePictureUrl() != null) existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());

        // updating role only if role is admin
        if (isAdmin && updatedUser.getRole() != null){
            existingUser.setRole(updatedUser.getRole());
        }

        UserEntity savedUser = userRepository.save(existingUser);



        if (updatedUser.getEmail() != null && !oldEmail.equals(updatedUser.getEmail())){
            emailVerificationService.handleEmailUpdate(savedUser, oldEmail, updatedUser.getEmail());
        }

        //save and return the updated User
        return savedUser;
    }


    private CredentialsEntity createCredentials(UserBaseEntity user, String password){
        CredentialsEntity credentials = new CredentialsEntity();
        credentials.setUser(user);
        credentials.setPassword(passwordEncoder.encode(password));
        credentialsRepository.save(credentials);
        return credentials;
    }
}
