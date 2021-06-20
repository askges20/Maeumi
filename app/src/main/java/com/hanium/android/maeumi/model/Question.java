package com.hanium.android.maeumi.model;

public class Question {
    int num;
    String content;

    public Question(int num, String content) {
        this.num = num;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
