package com.hanium.android.maeumi.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.view.loading.LoginUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification {
    private String title;   //알림 제목
    private String content; //알림 내용

    private String boardType;   //게시판 종류
    private String postNum; //게시글 번호

    private String dateTime;    //알림 시간

    private boolean isRead; //읽음 여부

    public Notification() {

    }

    public Notification(String title, String content, String boardType, String postNum, String dateTime) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.postNum = postNum;
        this.dateTime = dateTime;
        this.isRead = false;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("boardType", boardType);
        result.put("postNum", postNum);
        result.put("dateTime", dateTime);
        result.put("isRead", false);
        return result;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getBoardType() {
        return boardType;
    }

    public String getPostNum() {
        return postNum;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean getRead() {
        return isRead;
    }

    public void setRead(boolean read, String type) {
        isRead = read;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(5, 7);
        String date = dateTime.substring(8, 10);
        String hour = dateTime.substring(11, 13);
        String min = dateTime.substring(14, 16);
        String sec = dateTime.substring(17, 19);
        String pathDateTime = year + month + date + hour + min + sec;

        DatabaseReference notifyRef = database.getReference("/알림/" + LoginUser.getInstance().getUid()
                + "/" + pathDateTime + type + "/isRead/");
        notifyRef.setValue(true);
    }
}