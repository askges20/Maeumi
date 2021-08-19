package com.hanium.android.maeumi.view.heartprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanium.android.maeumi.R;
import androidx.appcompat.app.AppCompatActivity;

public class HeartGuide extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_guide);

        String fromTest = getIntent().getStringExtra("fromTest");
        if (fromTest == null) { //마음 채우기 화면에서 넘어왔을 경우
            TextView goHeartProgramBtn = findViewById(R.id.goToHeartProgramBtn);
            goHeartProgramBtn.setVisibility(View.GONE); //마음 채우기 화면으로 이동하는 버튼 삭제
        }
    }

    public void goToBack(View view){
        finish();
    }

    public void goToHeartProgram(View view) {
        Intent intent = new Intent(HeartGuide.this, HeartProgram.class);
        startActivity(intent);
        finish();
    }
}
