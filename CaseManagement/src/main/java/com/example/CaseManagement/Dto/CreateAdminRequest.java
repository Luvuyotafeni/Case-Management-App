package com.example.CaseManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CreateAdminRequest {

    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private String adminSpecificColumn;
    private String password;


    //getters and setters

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getProfilePictureUrl(){
        return this.profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl){
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getAdminSpecificColumn(){
        return this.adminSpecificColumn;
    }

    public void setAdminSpecificColumn(String adminSpecificColumn){
        this.adminSpecificColumn = adminSpecificColumn;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }


}
