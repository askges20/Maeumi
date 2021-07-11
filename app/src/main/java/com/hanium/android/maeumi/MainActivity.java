package com.hanium.android.maeumi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;
import com.hanium.android.maeumi.view.diary.DiaryContent;
import com.hanium.android.maeumi.view.diary.DiaryMain;
import com.hanium.android.maeumi.view.profile.Profile;
import com.hanium.android.maeumi.view.selftest.SelfTest;

public class MainActivity extends AppCompatActivity {

    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDiary(View view) {
        Intent intent = new Intent(MainActivity.this, DiaryMain.class);
        startActivity(intent);
    }

    public void goToSelfTest(View view) {
        Intent intent = new Intent(MainActivity.this, SelfTest.class);
        startActivity(intent);
    }

    public void goToChatBot(View view) {
        Intent intent = new Intent(MainActivity.this, ChatBot.class);
        startActivity(intent);
    }

    public void goToBoard(View view) {
        Intent intent = new Intent(MainActivity.this, Board.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {   //뒤로가기 버튼 클릭 시
        //super.onBackPressed();

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("앱을 종료하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        dialog.show();
    }
}