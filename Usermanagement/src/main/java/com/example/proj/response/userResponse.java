package com.example.proj.response;

import com.example.proj.entity.UserData;

import java.util.List;

public class userResponse {

    private int id;
    private String name;
    private String email;
    private String password;
    private List<UserData> userDatas;
    public userResponse(UserData userData){
        this.id = userData.getId();
        this.name = userData.getName();
        this.email = userData.getEmail();
        this.password  = userData.getPassword();
    }

    public userResponse(List<UserData> userData){
        this.userDatas = userData;
    }
}
