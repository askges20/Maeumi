package com.hanium.android.maeumi.view.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hanium.android.maeumi.R;

public class DiaryGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_guide);
    }

    public void goToBack(View view){
        finish();
    }
}