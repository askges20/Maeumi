package com.hanium.android.maeumi.view.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Diary;
import com.hanium.android.maeumi.viewmodel.DiaryMiddleViewModel;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.util.HashMap;
import java.util.Map;

public class DiaryContent extends AppCompatActivity {

    DiaryMiddleViewModel DiaryMiddleViewModel = new DiaryMiddleViewModel();

    String diaryCalDate, diaryFireDate;
    String diaryTitle, diaryContent, nullDiary;
    int diaryEmoticonNum;

    TextView dateText;  //날짜 텍스트
    TextView titleText; //제목 텍스트
    TextView contentText;   //내용 텍스트

    int year;
    int month;
    int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);

        DiaryMiddleViewModel.setDiaryData();
        diaryCalDate = DiaryMiddleViewModel.getCalendarDate();
        nullDate();

        dateText = findViewById(R.id.contentDate);
        titleText = findViewById(R.id.diaryTitle);
        contentText = findViewById(R.id.diaryContent);

        nullDiary = DiaryMiddleViewModel.getNullDiary();
        ifNullDiary();

        diaryTitle = DiaryMiddleViewModel.getTitle();
        diaryContent = DiaryMiddleViewModel.getContent();
        diaryEmoticonNum = DiaryMiddleViewModel.getEmoticonNum();

        dateText.setText(diaryCalDate);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);
    }
    // 일기 없을 때 이벤트
    public void ifNullDiary(){
        if (nullDiary == null){
            Toast.makeText(DiaryContent.this, "해당 날짜의 일기가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    // 빈곳 클릭 시 이벤트
    public void nullDate(){
        System.out.println("nullDate - "+ diaryCalDate);
        if(diaryCalDate == null){
            Toast.makeText(DiaryContent.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void goToDiaryModify(View view){ //수정 버튼 클릭 시
        Intent intent = new Intent(DiaryContent.this, DiaryModify.class);
        intent.putExtra("연", year);
        intent.putExtra("월", month);
        intent.putExtra("일", dayOfMonth);

        intent.putExtra("제목", titleText.getText());
        intent.putExtra("내용", contentText.getText());
        startActivity(intent);
        System.out.println("Move To Modify Diary");
    }

    public void showDeleteDialog(View view){    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryContent.this);
        dialog.setMessage("일기를 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                DiaryMiddleViewModel.deleteDiary();

                Toast toastView = Toast.makeText(DiaryContent.this, "삭제 완료", Toast.LENGTH_SHORT);
                toastView.show();
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(DiaryContent.this,"삭제 취소", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }

}