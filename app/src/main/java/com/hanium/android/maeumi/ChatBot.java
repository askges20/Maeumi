package com.hanium.android.maeumi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChatBot extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_main);
    }

    public void goToChatBotStart(View view){
        Intent intent = new Intent(ChatBot.this, ChatBotStart.class);
        startActivity(intent);
        System.out.println("Move To ChatBotStart");
    }

}