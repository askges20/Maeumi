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
    LoginUser loginUser = LoginUser.getInstance();

    public static String victimScore;
    public static String perpetrationScore;

    public TestModel(){}

    public TestModel(TestHistory history){
        this.TestHistory = history;
    }

    public String getVictimScore(){
        return victimScore;
    }
    public String getPerpetrationScore(){
        return perpetrationScore;
    }

    public void setVictimScore(String score){
        this.victimScore = score;
        System.out.println(this.victimScore);
//        TestHistory.setVictimScore(this.victimScore);
    }



    public void getHistory() {

        database = FirebaseDatabase.getInstance();
        victim = database.getReference("/진단테스트/" + loginUser.getUid() + "/피해정도");
        perpetration = database.getReference("/진단테스트/" + loginUser.getUid() + "/가해정도");

        victim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                victimScore = String.valueOf(dataSnapshot.getValue());

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