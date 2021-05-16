package com.hanium.android.maeumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Main Main");
    }

    public void goToDiary(View view){
        Intent intent = new Intent(MainActivity.this, Diary.class);
        startActivity(intent);
        System.out.println("Move To Diary");
    }

    public void goToSelfTest(View view){
        Intent intent = new Intent(MainActivity.this, SelfTest.class);
        startActivity(intent);
        System.out.println("Move To SelfTest");
    }

    public void goToChatBot(View view){
        Intent intent = new Intent(MainActivity.this, ChatBot.class);
        startActivity(intent);
        System.out.println("Move To ChatBot");
    }

    public void goToBoard(View view){
        Intent intent = new Intent(MainActivity.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
    }

    public void goToProfile(View view){
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
        System.out.println("Move To Profile");
    }

}