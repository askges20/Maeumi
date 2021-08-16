package com.hanium.android.maeumi.model;

import java.util.HashMap;
import java.util.Map;

public class Notification {
    private String title;   //알림 제목
    private String content; //알림 내용

    private String postNum; //게시글 번호
    private String comment; //댓글 내용

    private String dateTime;    //알림 시간

    private boolean isRead; //읽음 여부

    public Notification() {

    }

    public Notification (String title, String content, String postNum, String comment, String dateTime) {
        this.title= title;
        this.content = content;
        this.postNum = postNum;
        this.comment = comment;
        this.dateTime = dateTime;
        this.isRead = false;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("postNum", postNum);
        result.put("comment", comment);
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

    public String getPostNum() {
        return postNum;
    }

    public String getComment() {
        return comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean isRead() {
        return isRead;
    }
}