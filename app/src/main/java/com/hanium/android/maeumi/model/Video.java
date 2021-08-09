package com.hanium.android.maeumi.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.view.loading.LoginUser;

public class Video {
    private String id;  //영상 id
    private String title;   //제목
    private String description; //설명
    private boolean isWatched = false;  //시청 완료 여부, 디폴트는 false

    public Video(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        //readFromFB();
    }

    //DB에서 시청 기록 읽어오기
    private void readFromFB() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference videoRef = firebaseDatabase.getReference("/마음채우기/"+ LoginUser.getInstance().getUid()+"/"+id);
        if (videoRef.getKey() != null){
            isWatched = true;
            System.out.println("비ㅇㄷㅁㅇ러ㅑ"+id+"시청완료");
        }
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
