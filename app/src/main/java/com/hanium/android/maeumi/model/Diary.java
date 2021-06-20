package com.hanium.android.maeumi.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Diary {
    /*일기 데이터*/

    //날짜
    int year;
    int month;
    int dayOfMonth;

    //제목, 내용, 이모티콘 번호
    public String title;
    public String content;
    public int emoticonNum;

    public Diary(){
    }

    public Diary(String title, String content, int emoticonNum){
        this.title = title;
        this.content = content;
        this.emoticonNum = emoticonNum;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("emoticonNum", emoticonNum);
        return result;
    }
}
