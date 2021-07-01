package com.hanium.android.maeumi.model;

public class Comment {
    public String writer;   //작성자 아이디
    public String content;  //댓글 내용
    public String writeDate;    //작성일자
    public String postCode; //게시글 번호(코드)

    public Comment(String writer, String content, String writeDate, String postCode) {
        this.writer = writer;
        this.content = content;
        this.writeDate = writeDate;
        this.postCode = postCode;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
