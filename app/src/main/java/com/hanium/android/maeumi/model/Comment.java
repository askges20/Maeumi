package com.hanium.android.maeumi.model;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String writer;   //작성자 아이디
    public String content;  //댓글 내용
    public String writeDate;    //작성일자
    public String writerUid;    //작성자 uid
    
    public Comment(){

    }

    public Comment(String writer, String content, String writeDate, String writerUid) {
        this.writer = writer;
        this.content = content;
        this.writeDate = writeDate;
        this.writerUid = writerUid;
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

    public String getWriterUid() {
        return writerUid;
    }

    public void setWriterUid(String writerUid) {
        this.writerUid = writerUid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("writer", writer);
        result.put("content", content);
        result.put("writeDate", writeDate);
        result.put("writerUid", writerUid);
        return result;
    }
}
