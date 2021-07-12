package com.hanium.android.maeumi.model;


import com.hanium.android.maeumi.LoginUser;

public class ProfileModel {

    LoginUser loginUser = LoginUser.getInstance();

    String userName, userEmail, userAlias, userSchool, userGender;


    public void getUserInfo(){
        loginUser.getUid();

        userName = loginUser.getName();
    }
}
