package com.hanium.android.maeumi.view.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class DiaryContent extends AppCompatActivity {

    DiaryModel DiaryModel = new DiaryModel();
    FirebaseStorage storage;
    StorageReference storageRef;

    String diaryCalDate, diaryTitle, diaryContent, nullDiary, diaryEmoticonNum;
    TextView dateText, titleText, contentText, emoticon;
    ImageViewZoom imgView;
    ConstraintLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);

        DiaryModel.setDiaryData();
        diaryCalDate = DiaryModel.getCalendarDate();
        nullDiary = DiaryModel.getNullDiary();

        dateText = findViewById(R.id.contentDate);
        titleText = findViewById(R.id.diaryTitle);
        contentText = findViewById(R.id.diaryContent);
        emoticon = findViewById(R.id.emoticon);
        mainContent = findViewById(R.id.mainContent);
        imgView = findViewById(R.id.testImgView);


        diaryTitle = DiaryModel.getTitleTwo();
        diaryContent = DiaryModel.getContent();
        diaryEmoticonNum = DiaryModel.getEmoticonNum();

        checkNull();

        dateText.setText(diaryCalDate);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);
        if (diaryEmoticonNum != null) {
            switch (diaryEmoticonNum) {
                case "1":
                    emoticon.setText("좋음");
                    mainContent.setBackgroundResource(R.color.pinkred);
                    break;
                case "2":
                    emoticon.setText("평범");
                    mainContent.setBackgroundResource(R.color.lightgreen);
                    break;
                case "3":
                    emoticon.setText("나쁨");
                    mainContent.setBackgroundResource(R.color.diaryGray);
                    break;
            }
        }
        getImg();
    }
    public void checkNull() {
        // 빈곳 클릭 시 이벤트
        if (diaryCalDate == null) {
            Toast.makeText(DiaryContent.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
        // 일기 없을 때 이벤트
        if (nullDiary == null && diaryEmoticonNum == null) {
            finish();
        }
    }

    public void goToDiaryModify(View view) { //수정 버튼 클릭 시
        Intent intent = new Intent(DiaryContent.this, DiaryModify.class);
        startActivity(intent);
    }

    public void showDeleteDialog(View view) {    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryContent.this);
        dialog.setMessage("일기를 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                DiaryModel.deleteDiary(diaryEmoticonNum);
                deleteImg();

                Toast toastView = Toast.makeText(DiaryContent.this, "삭제 완료", Toast.LENGTH_SHORT);
                toastView.show();
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(DiaryContent.this, "삭제 취소", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    //목록으로 버튼 클릭 시
    public void goToBack(View view) {
        finish();
    }

    // 이미지 조회
    private void getImg() {
        String imgString = DiaryModel.getFireImgName();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("/diary");

        storageRef.child(imgString).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext()).load(uri).into(imgView);
            }
        });
    }
    private void deleteImg(){
        String imgString = DiaryModel.getFireImgName();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("/diary");

        storageRef.child(imgString).delete();
    }

}