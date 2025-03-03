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

    //getters and setters
    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getProfilePictureUrl(){
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl){
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }

}
