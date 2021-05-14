package com.hanium.android.maeumi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Diary extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_main);
    }

    public void goToDiaryContent(View view){
        Intent intent = new Intent(Diary.this, DiaryContent.class);
        startActivity(intent);
        System.out.println("Move To Diary Content");
    }

}
