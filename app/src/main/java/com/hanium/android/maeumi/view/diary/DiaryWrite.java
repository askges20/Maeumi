package com.hanium.android.maeumi.view.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Diary;

import java.util.HashMap;
import java.util.Map;

public class DiaryWrite extends Activity {
    TextView date;
    EditText diaryTitleText;
    EditText diaryContentText;

    int year;
    int month;
    int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        //날짜 받아오기
        Intent dateIntent = getIntent();
        year = dateIntent.getIntExtra("연", 1);
        month = dateIntent.getIntExtra("월", 1);
        dayOfMonth = dateIntent.getIntExtra("일", 1);;
        date = findViewById(R.id.writeDate);
        date.setText(year + "." + month + "." + dayOfMonth);

        diaryTitleText = (EditText)findViewById(R.id.diaryTitleText);
        diaryContentText = (EditText)findViewById(R.id.diaryContentText);
    }

    //작성 완료 버튼 클릭 시 DB 반영   -> 추후 view가 아닌 다른 패키지로 옮길 예정
    public void processAdd(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference diaryRef = database.getReference("일기장");

        /*DB 반영 구현 중*/

        //작성한 일기 제목, 내용
        String diaryTitle = diaryTitleText.getText().toString();
        String diaryContent = diaryContentText.getText().toString();

        Toast.makeText(DiaryWrite.this, diaryTitle, Toast.LENGTH_SHORT).show();
        Toast.makeText(DiaryWrite.this, diaryContent, Toast.LENGTH_SHORT).show();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;
        Diary diary = new Diary(diaryTitle, diaryContent, 1);
        diaryValues = diary.toMap();

        childUpdates.put("/id/"+date, diaryValues);
        diaryRef.updateChildren(childUpdates);


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
