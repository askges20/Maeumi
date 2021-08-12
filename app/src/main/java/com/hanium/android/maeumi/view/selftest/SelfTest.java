package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.loading.LoginUser;

public class SelfTest extends AppCompatActivity {

    private Button startTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selftest);

        startTestBtn = findViewById(R.id.startTestBtn);
        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   //현재 액티비티 종료
                Intent intent = new Intent(SelfTest.this, TestClick.class);
                startActivity(intent); //액티비티 이동
            }
        });

    }
    public void goToHistory(View view){
        String victimScore = LoginUser.getInstance().getVictimScore();

        if (victimScore == null) {    //결과가 존재하지 않음
            Toast.makeText(this, "테스트 결과가 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
        } else {    //결과가 존재함
            Intent intent = new Intent(SelfTest.this, TestResult.class);
            startActivity(intent);
        }
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}