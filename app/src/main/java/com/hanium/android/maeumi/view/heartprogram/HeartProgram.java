package com.hanium.android.maeumi.view.heartprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hanium.android.maeumi.R;

public class HeartProgram extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_program);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}