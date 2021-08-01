package com.hanium.android.maeumi;

import com.hanium.android.maeumi.model.TestModel;

public class LoginUser {
    private String uid;
    private String email;
    private String name;
    private String gender;
    private String school;
    private String alias;

    private static LoginUser user = new LoginUser();

    private TestModel testModel = new TestModel();  //테스트 결과 관련 모델

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


    /* 테스트 결과 불러와서 저장하기 */
    public void setHistory() {
        testModel.getHistory();
    }

    public String getVictimScore(){
        return testModel.getVictimScore();
    }

    public String getPerpetrationScore(){
        return testModel.getPerpetrationScore();
    }
}
