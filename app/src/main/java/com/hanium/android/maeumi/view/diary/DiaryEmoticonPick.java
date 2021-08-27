package com.hanium.android.maeumi.view.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;

public class DiaryEmoticonPick extends AppCompatActivity {

    DiaryModel DiaryModel = new DiaryModel();

    LinearLayout glad, happy, calm, angry, sad, worried;
    String diaryTitle, diaryContent, diaryEmoticonNum;
    Bitmap diaryImgBitmap;
    Button diaryEmoticonPickDone;
    Uri diaryImgUri;

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

        diaryImgBitmap = DiaryModel.getImgNameBitmap();
        diaryImgUri = DiaryModel.getImgNameUri();

        diaryTitle = diaryIntent.getStringExtra("title");
        diaryContent = diaryIntent.getStringExtra("content");

    }

    public void onEmoticonClick(View view) {
        int hello = view.getId();
        view.setBackgroundResource(R.color.pinkred);

        if (hello != glad.getId()) {
            glad.setBackgroundResource(R.color.white);
        } else {
            diaryEmoticonNum = "1";
        }

        if (hello != happy.getId()) {
            happy.setBackgroundResource(R.color.white);
        } else {
            diaryEmoticonNum = "2";
        }

        if (hello != calm.getId()) {
            calm.setBackgroundResource(R.color.white);
        } else {
            diaryEmoticonNum = "3";
        }

        if (hello != angry.getId()) {
            angry.setBackgroundResource(R.color.white);
        } else {
            diaryEmoticonNum = "4";
        }

        if (hello != sad.getId()) {
            sad.setBackgroundResource(R.color.white);
        } else {
            diaryEmoticonNum = "5";
        }

        if (hello != worried.getId()) {
            worried.setBackgroundResource(R.color.white);
        } else {
            diaryEmoticonNum = "6";
        }
    }

    public void writeDiary(View view) {
        if (diaryEmoticonNum == null) {
            Toast.makeText(DiaryEmoticonPick.this, "기분을 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            if (diaryImgBitmap != null) {
                //사진 추가,변경 되었으면
                DiaryModel.saveImg(diaryImgBitmap);
            } else if(diaryImgUri != null){
                // 기존 이미지 그대로 유지
            }else if(diaryImgBitmap == null && diaryImgUri == null){
                // 사진 없거나 제거했다면
                deleteImg();
            }

            Toast.makeText(DiaryEmoticonPick.this, "일기 작성완료", Toast.LENGTH_SHORT).show();
            DiaryModel.diaryWrite(diaryTitle, diaryContent, diaryEmoticonNum);
            finish();
        }

    }
    private void deleteImg() {
        String imgString = DiaryModel.getFireImgName();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("/diary");
        storageRef.child(imgString).delete();
    }

    public void goToBack(View view) {
        finish();
    }
}