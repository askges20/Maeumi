package com.hanium.android.maeumi.model;

public class Post {

    //게시물 firebase json 데이터 구조 확정되고 나서 변수, Getter, Setter 수정할 것

    String title;       //제목
    String content;     //내용
    String writer;      //작성자
    String writeDate;   //작성일자

    public Post(String title, String writer, String writeDate) {
        this.title = title;
        this.writer = writer;
        this.writeDate = writeDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}
