package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;
import com.hanium.android.maeumi.view.diary.DiaryMain;
import com.hanium.android.maeumi.view.profile.Profile;
import com.hanium.android.maeumi.view.selftest.SelfTest;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Main Main");

        System.out.println("데이터베이스 :" + database);
        System.out.println("레퍼런스 : " + myRef);
    }

    public void goToDiary(View view) {
        Intent intent = new Intent(MainActivity.this, DiaryMain.class);
        startActivity(intent);
        System.out.println("Move To Diary");
    }

    public void goToSelfTest(View view) {
        Intent intent = new Intent(MainActivity.this, SelfTest.class);
        startActivity(intent);
        System.out.println("Move To SelfTest");
    }

    public void goToChatBot(View view) {
        Intent intent = new Intent(MainActivity.this, ChatBot.class);
        startActivity(intent);
        System.out.println("Move To ChatBot");
    }

    public void goToBoard(View view) {
        Intent intent = new Intent(MainActivity.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
        System.out.println("Move To Profile");
    }
}