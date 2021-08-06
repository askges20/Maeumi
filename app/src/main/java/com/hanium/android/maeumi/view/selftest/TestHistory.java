package com.hanium.android.maeumi.view.selftest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;

public class TestHistory extends AppCompatActivity {

    public static String victimScore;
    public static String perpetrationScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);

        victimScore = LoginUser.getInstance().getVictimScore();
        perpetrationScore = LoginUser.getInstance().getPerpetrationScore();

        if (victimScore == null) {    //결과가 존재하지 않음
            alertNoResult();
        } else {    //결과가 존재함
            moveToTestResult();
        }
    }

    //테스트 결과가 존재하지 않음을 알림
    public void alertNoResult() {
        Toast.makeText(this, "테스트 결과가 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
        finish();
    }

    //테스트 결과 페이지로 이동
    public void moveToTestResult() {
        Intent intent = new Intent(TestHistory.this, TestResult.class);
        finish();
        startActivity(intent);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}