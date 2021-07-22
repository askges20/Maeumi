package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

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
        Intent intent = new Intent(this,TestHistory.class);
        startActivity(intent);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}