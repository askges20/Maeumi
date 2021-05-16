package com.hanium.android.maeumi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DiaryContent extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);
    }

    public void goToDiaryModify(View view){
        Intent intent = new Intent(DiaryContent.this, DiaryModify.class);
        startActivity(intent);
        System.out.println("Move To Modify Diary");
    }
}
