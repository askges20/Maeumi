package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.loading.LoginUser;

public class SelfTest extends AppCompatActivity {

    private Button showTestResultBtn;
    private Button startTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selftest);

        showTestResultBtn = findViewById(R.id.showTestResultBtn);
        startTestBtn = findViewById(R.id.startTestBtn);

        String victimScore = LoginUser.getInstance().getVictimScore();  //테스트 결과 피해 정도 조회

        if (victimScore == null) {    //결과가 존재하지 않음
            showTestResultBtn.setVisibility(View.GONE); //지난 테스트 결과 확인 버튼 없애기
            startTestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();   //현재 액티비티 종료
                    Intent intent = new Intent(SelfTest.this, TestClick.class);
                    startActivity(intent); //액티비티 이동
                }
            });
        } else {    //이미 테스트를 완료함
            startTestBtn.setVisibility(View.GONE);  //테스트 시작 버튼 없애기
        }

    }

    public void goToHistory(View view) {
        Intent intent = new Intent(SelfTest.this, TestResult.class);
        startActivity(intent);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}