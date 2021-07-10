package com.hanium.android.maeumi;

public class Message {
    private String message; //채팅 내용
    private boolean isReceived; //사용자가 보낸 채팅인지 Dialogflow가 보낸 채팅인지 구분

    public Message(String message, boolean isReceived) {
        this.message = message;
        this.isReceived = isReceived;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }
}
