package com.hanium.android.maeumi.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.view.selftest.TestHistory;

public class TestModel {
    TestHistory TestHistory;

    FirebaseDatabase database;
    DatabaseReference victim;
    DatabaseReference perpetration;

    //LoginUser loginUser = LoginUser.getInstance();
    //ActivitySplash에서 사용자 정보를 가져오기 전에 생성되므로 null값을 가지는 오류 발생 -> 주석 처리

    public static String victimScore;
    public static String perpetrationScore;

    public TestModel() {
    }

    public TestModel(TestHistory history) {
        this.TestHistory = history;
    }

    public String getVictimScore() {
        return victimScore;
    }

    public String getPerpetrationScore() {
        return perpetrationScore;
    }

    public void setVictimScore(String score) {
        this.victimScore = score;
        System.out.println(this.victimScore);
    }


    public void getHistory() {
        LoginUser loginUser = LoginUser.getInstance();

        database = FirebaseDatabase.getInstance();
        victim = database.getReference("/진단테스트/" + loginUser.getUid() + "/피해정도");
        perpetration = database.getReference("/진단테스트/" + loginUser.getUid() + "/가해정도");

        victim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setVictimScore(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });

        perpetration.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                perpetrationScore = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }


}