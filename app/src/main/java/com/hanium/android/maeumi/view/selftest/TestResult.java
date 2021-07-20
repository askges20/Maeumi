package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.MainActivity;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;

public class TestResult extends AppCompatActivity {

    int victimScore = 0;    //피해 정도
    int perpetrationScore = 0;  //가해 정도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        TextView victimScoreText = findViewById(R.id.victimScore);
        TextView perpetrationScoreText = findViewById(R.id.perpetrationScore);

        victimScore = getIntent().getIntExtra("victim", 0);
        victimScoreText.setText("" + victimScore);
        perpetrationScore = getIntent().getIntExtra("perpetration", 0);
        perpetrationScoreText.setText("" + perpetrationScore);
    }


    public void goToChatBotStart(View view) {
        Intent intent = new Intent(TestResult.this, ChatBot.class);
        startActivity(intent);
        System.out.println("Move To ChatBotStart");
    }


    public void goToBoard(View view) {
        Intent intent = new Intent(TestResult.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
    }

    public void goToMain(View view) {
        Intent intent = new Intent(TestResult.this, MainActivity.class);
        startActivity(intent);
        System.out.println("Move To Main");
    }

}

