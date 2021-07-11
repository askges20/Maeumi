package com.hanium.android.maeumi;

public class LoginUser {
    private String uid;
    private String email;
    private String name;

    private static final LoginUser user = new LoginUser();

    public static LoginUser getInstance() {
        return user;
    }

    private LoginUser() {

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
}
