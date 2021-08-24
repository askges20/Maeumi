package com.hanium.android.maeumi.view.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;

public class DiaryEmoticonPick extends AppCompatActivity {

    DiaryModel Diarymodel = new DiaryModel();

    LinearLayout glad, happy, calm, angry, sad, worried;
    String diaryTitle, diaryContent,  diaryEmoticonNum;
    Bitmap diaryImgName;
    Button diaryEmoticonPickDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_emoticon_pick);

        glad = findViewById(R.id.glad);
        happy = findViewById(R.id.happy);
        calm = findViewById(R.id.calm);
        angry = findViewById(R.id.angry);
        sad = findViewById(R.id.sad);
        worried = findViewById(R.id.worried);
        diaryEmoticonPickDone = findViewById(R.id.diaryEmoticonPickDone);

        Intent diaryIntent = getIntent();
        if(getIntent().getByteArrayExtra("imgName") != null){
            byte[] arr = getIntent().getByteArrayExtra("imgName");
            diaryImgName = BitmapFactory.decodeByteArray(arr,0,arr.length);
        }
        diaryTitle = diaryIntent.getStringExtra("title");
        diaryContent = diaryIntent.getStringExtra("content");

    }

    public void onEmoticonClick(View view) {
        int hello = view.getId();
        view.setBackgroundResource(R.color.pinkred);

        if(hello != glad.getId()){
            glad.setBackgroundResource(R.color.white);
        }else{
            diaryEmoticonNum = "1";
        }

        if(hello != happy.getId()){
            happy.setBackgroundResource(R.color.white);
        }else{
            diaryEmoticonNum = "2";
        }

        if(hello != calm.getId()){
            calm.setBackgroundResource(R.color.white);
        }else{
            diaryEmoticonNum = "3";
        }

        if(hello != angry.getId()){
            angry.setBackgroundResource(R.color.white);
        }else{
            diaryEmoticonNum = "4";
        }

        if(hello != sad.getId()){
            sad.setBackgroundResource(R.color.white);
        }else{
            diaryEmoticonNum = "5";
        }

        if(hello != worried.getId()){
            worried.setBackgroundResource(R.color.white);
        }else{
            diaryEmoticonNum = "6";
        }
    }

    public void writeDiary(View view) {
        Diarymodel.diaryWrite(diaryTitle,diaryContent,diaryEmoticonNum);
        Diarymodel.setImgName(diaryImgName);
        finish();
    }

    public void goToBack(View view) {
        finish();
    }
}