package com.hanium.android.maeumi;

public class LoginUser {
    private String uid;
    private String email;
    private String name;
    private String gender;
    private String school;
    private String alias;

    //private static final LoginUser user = new LoginUser();
    private static LoginUser user = new LoginUser();

    public static LoginUser getInstance() {
        return user;
    }

    public LoginUser() {

    }

    public static void signOutUser() {  //로그아웃 진행
        user = new LoginUser();
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
