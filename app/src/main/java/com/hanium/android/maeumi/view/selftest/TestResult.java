package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBotStart;

public class TestResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
    }


    public void goToChatBotStart(View view) {
        Intent intent = new Intent(TestResult.this, ChatBotStart.class);
        startActivity(intent);
        System.out.println("Move To ChatBotStart");
    }


    public void goToBoard(View view){
        Intent intent = new Intent(TestResult.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
    }

    }

