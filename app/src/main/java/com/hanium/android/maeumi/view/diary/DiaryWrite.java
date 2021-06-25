package com.hanium.android.maeumi.view.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Diary;
import com.hanium.android.maeumi.viewmodel.DiaryMiddleViewModel;

import java.util.HashMap;
import java.util.Map;

public class DiaryWrite extends AppCompatActivity {

    DiaryMiddleViewModel DiaryMiddleViewModel = new DiaryMiddleViewModel();

    TextView date;
    EditText diaryTitleText;
    EditText diaryContentText;

    String diaryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryDate = DiaryMiddleViewModel.getCalendarDate();
        nullDate();

        date = findViewById(R.id.writeDate);
        diaryTitleText = (EditText)findViewById(R.id.diaryTitleWriteText);
        diaryContentText = (EditText)findViewById(R.id.diaryContentWriteText);
        date.setText(diaryDate);
    }
    // 빈곳 클릭 시 이벤트
    public void nullDate(){
        System.out.println("nullDate - "+ diaryDate);
        if(diaryDate == null){
            Toast.makeText(DiaryWrite.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //작성 완료 버튼 클릭 시 DB 반영   -> 추후 view가 아닌 다른 패키지로 옮길 예정
    public void processAdd(View view){

        //작성한 일기 제목, 내용
        String diaryTitle = diaryTitleText.getText().toString();
        String diaryContent = diaryContentText.getText().toString();

        DiaryMiddleViewModel.diaryWrite(diaryTitle,diaryContent);

        Toast toastView = Toast.makeText(DiaryWrite.this, "작성 완료", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
}
