package com.hanium.android.maeumi.view.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.diary.DiaryMain;

public class ChatBotStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_start);
    }
    public void goToDiary(View view){
        Intent intent = new Intent(ChatBotStart.this, DiaryMain.class);
        startActivity(intent);
        System.out.println("Move To Diary");
    }
    public void goToBoardAnonymous(View view){
        Intent intent = new Intent(ChatBotStart.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
        //익명게시판으로 이동하도록 코드 추가 필요
    }
}