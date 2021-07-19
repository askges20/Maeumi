package com.hanium.android.maeumi.model;

public class Message {
    private String content; //메세지 내용
    private boolean isReceived; //bot으로부터 받은 메세지인지
    private boolean isFirstMsgOfDay;    //해당 날짜의 첫번째 채팅인지

    private int year;
    private int month;
    private int date;

    public Message(String content, boolean isReceived, boolean isFirstMsgOfDay) {
        this.content = content;
        this.isReceived = isReceived;
        this.isFirstMsgOfDay = isFirstMsgOfDay;
    }

    //채팅 조회할 때 사용되는 생성자
    public Message(String content, boolean isReceived, boolean isFirstMsgOfDay, int year, int month, int date) {
        this.content = content;
        this.isReceived = isReceived;
        this.isFirstMsgOfDay = isFirstMsgOfDay;
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }

    public boolean isFirstMsgOfDay() {
        return isFirstMsgOfDay;
    }

    public void setFirstMsgOfDay(boolean firstMsgOfDay) {
        isFirstMsgOfDay = firstMsgOfDay;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
