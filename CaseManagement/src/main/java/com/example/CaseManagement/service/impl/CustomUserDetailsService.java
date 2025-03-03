package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.CredentialsEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.repository.CredentialsRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        UserBaseEntity user = userBaseRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not found with email " + email);
        }

        CredentialsEntity credentials = credentialsRepository.findByUser(user);
        if (credentials == null) {
            throw new UsernameNotFoundException("credentials not found for user: " + email);
        }

        return new User(
                user.getEmail(),
                credentials.getPassword(),
                Collections.singletonList( new SimpleGrantedAuthority("ROLE" + user.getRole().name()))
        );

    }
}
