package com.hanium.android.maeumi.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.view.loading.ActivitySplash;
import com.hanium.android.maeumi.view.loading.LoginUser;

public class TestModel {
    FirebaseDatabase database;
    DatabaseReference testRef;

    public static String victimScore;
    public static String perpetrationScore;

    public TestModel() {
    }

    public String getVictimScore() {
        return victimScore;
    }

    public String getPerpetrationScore() {
        return perpetrationScore;
    }

    public void setVictimScore(String score) {
        this.victimScore = score;
    }

    public void setPerpetrationScore(String score) {
        this.perpetrationScore = score;
    }

    public void getHistory(ActivitySplash splash) {
        LoginUser loginUser = LoginUser.getInstance();

        database = FirebaseDatabase.getInstance();
        testRef = database.getReference("/진단테스트/" + loginUser.getUid() + "/");
        testRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.getKey().equals("피해정도")){
                        setVictimScore(snap.getValue().toString());
                    } else if(snap.getKey().equals("가해정도")){
                        setPerpetrationScore(snap.getValue().toString());
                    }
                    System.out.println("사용자 테스트 결과 불러오기 완료");
                }

                splash.moveToMain();    //테스트 결과 읽어왔으므로 스플래시 -> 메인 화면으로 이동
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}