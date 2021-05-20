package com.hanium.android.maeumi.view.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

public class ChatBotKeyWord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_keyword);
    }
    public void goToChatBotKeyWordYES(View view){
        Intent intent = new Intent(ChatBotKeyWord.this, ChatBotKeyWordYES.class);
        System.out.println("Move To ChatBotKeyWordYES");
    }
    public void goToChatBotKeyWordNO(View view){
        Intent intent = new Intent(ChatBotKeyWord.this, ChatBotKeyWordNO.class);
        System.out.println("Move To ChatBotKeyWordNO");
    }
}
