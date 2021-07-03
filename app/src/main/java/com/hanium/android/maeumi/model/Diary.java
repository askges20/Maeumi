package com.hanium.android.maeumi.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Diary {
    //일기 데이터
    public String title;
    public String content;
    public String emoticonNum;
    public String date;

    public Diary(){
    }

    public Diary(String title, String content, String emoticonNum,String date){
        this.title = title;
        this.content = content;
        this.emoticonNum = emoticonNum;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("emoticonNum", emoticonNum);
        result.put("date", date);
        return result;
    }
}