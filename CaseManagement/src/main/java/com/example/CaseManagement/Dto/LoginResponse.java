package com.example.CaseManagement.Dto;

import com.example.CaseManagement.entity.UserBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String userId;
    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private String role;

    //constructor
    public LoginResponse(UserBaseEntity user){
        this.userId = user.getUserId().toString();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.role = user.getRole().toString();
    }

}
