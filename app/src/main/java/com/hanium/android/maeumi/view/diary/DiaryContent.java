package com.hanium.android.maeumi.view.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

    String diaryCalDate, diaryTitle, diaryContent, nullDiary, diaryEmoticonNum,diaryWriteDate;
    TextView dateText, titleText, contentText,diaryWDate;
    ImageViewZoom imgView;
    ImageView emoticon;
    ConstraintLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);

        DiaryModel.setDiaryData();
        diaryCalDate = DiaryModel.getCalendarDate();
        nullDiary = DiaryModel.getNullDiary();

        dateText = findViewById(R.id.contentDate);
        diaryWDate = findViewById(R.id.diaryWriteDate);
        titleText = findViewById(R.id.diaryTitle);
        contentText = findViewById(R.id.diaryContent);
        emoticon = findViewById(R.id.emoticon);
        mainContent = findViewById(R.id.mainContent);
        imgView = findViewById(R.id.testImgView);


        diaryTitle = DiaryModel.getTitleTwo();
        diaryContent = DiaryModel.getContent();
        diaryEmoticonNum = DiaryModel.getEmoticonNum();
        diaryWriteDate = DiaryModel.getDiaryWriteDate();

        checkNull();

        dateText.setText(diaryCalDate);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);
        if(diaryWriteDate == null){
            diaryWDate.setText("작성일 조회 실패");
        }else{
            diaryWDate.setText("작성일: "+diaryWriteDate);
        }
        if (diaryEmoticonNum != null) {
            switch (diaryEmoticonNum) {
                case "1":
                    emoticon.setImageResource(R.drawable.diary_glad);
                    break;
                case "2":
                    emoticon.setImageResource(R.drawable.diary_happy);
                    break;
                case "3":
                    emoticon.setImageResource(R.drawable.diary_calm);
                    break;
                case "4":
                    emoticon.setImageResource(R.drawable.diary_angry);
                    break;
                case "5":
                    emoticon.setImageResource(R.drawable.diary_sad);
                    break;
                case "6":
                    emoticon.setImageResource(R.drawable.diary_worried);
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
        finish();
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