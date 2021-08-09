package com.hanium.android.maeumi.model;

public class Video {
    private String id;  //영상 id
    private String title;   //제목
    private String description; //설명

    public Video(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
