package com.hanium.android.maeumi.model;

public class Video {
    private String id;  //영상 id
    private String title;   //제목
    private String description; //설명
    private boolean isWatched = false;  //시청 완료 여부, 디폴트는 false

    public Video() {

    }

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

    public boolean getIsWatched() {
        return isWatched;
    }
}
