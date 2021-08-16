package com.hanium.android.maeumi.model;

public class Notification {
    private String userUid; //받는 사람 uid
    private String title;   //알림 제목
    private String content; //알림 내용

    private String postNum; //게시글 번호
    private String comment; //댓글 내용

    private boolean isRead; //읽음 여부

    public Notification (String userUid, String title, String content, String postNum, String comment) {
        this.userUid = userUid;
        this.title= title;
        this.content = content;
        this.postNum = postNum;
        this.comment = comment;
        this.isRead = false;
    }
}