package com.example.CaseManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private String password;

}