package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;

public class TestResult extends AppCompatActivity {

    int victimScore = 0;    //피해 정도
    int perpetrationScore = 0;  //가해 정도

    ProgressBar victimProgress;
    ProgressBar perpetrationProgress;

    TextView victimResultDetailText,victimScoreText;
    TextView perpetrationResultDetailText,perpetrationScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        victimScoreText = findViewById(R.id.victimScore);
        perpetrationScoreText = findViewById(R.id.perpetrationScore);

        victimScore = getIntent().getIntExtra("victim", 0);
        if (victimScore == 0) {
            victimScore = Integer.parseInt(LoginUser.getInstance().getVictimScore());
        }

        perpetrationScore = getIntent().getIntExtra("perpetration", 0);
        if (perpetrationScore == 0){
            perpetrationScore = Integer.parseInt(LoginUser.getInstance().getPerpetrationScore());
        }


        //프로그레스바 나타내기
        victimProgress = findViewById(R.id.victimProgressBar);
        victimProgress.setMax(36);
        victimProgress.setMin(0);
        victimProgress.setProgress(victimScore);

        perpetrationProgress = findViewById(R.id.perpetrationProgressBar);
        perpetrationProgress.setMax(36);
        perpetrationProgress.setMin(0);
        perpetrationProgress.setProgress(perpetrationScore);


        victimResultDetailText = findViewById(R.id.victimResultDetailText);
        perpetrationResultDetailText = findViewById(R.id.perpetrationResultDetailText);

        setTestResult();    //결과 상세 내용 텍스트 반영
    }

    public void setTestResult() {
        //피해 정도 결과
        if (victimScore <= 6){  //0~6점 아주 약함
            victimResultDetailText.setText(R.string.test_result_victim_1);
            victimScoreText.setText(" 아주 약함");
        } else if (victimScore <= 13){  //7~13점 약함
            victimResultDetailText.setText(R.string.test_result_victim_2);
            victimScoreText.setText(" 약함");
        } else if (victimScore <= 22){  //14~22점 보통
            victimResultDetailText.setText(R.string.test_result_victim_3);
            victimScoreText.setText(" 보통");
        } else if (victimScore <= 29){  //23~29점 심함
            victimResultDetailText.setText(R.string.test_result_victim_4);
            victimScoreText.setText(" 심함");
        } else {    //30~36점 아주 심함
            victimResultDetailText.setText(R.string.test_result_victim_5);
            victimScoreText.setText(" 아주 심함");
        }

        //가해 정도 결과
        if (perpetrationScore <= 6){
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_1);
            perpetrationScoreText.setText("아주 약함");
        } else if (perpetrationScore <= 13){
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_2);
            perpetrationScoreText.setText("약함");
        } else if (perpetrationScore <= 22){
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_3);

        } else if (perpetrationScore <= 29){
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_4);
            perpetrationScoreText.setText("심함");
        } else {
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_5);
            perpetrationScoreText.setText("아주 심함");
        }
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
        finish();
        System.out.println("Move To Main");
    }

}

