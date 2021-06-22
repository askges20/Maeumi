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
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.util.HashMap;
import java.util.Map;

public class DiaryWrite extends AppCompatActivity {

    DiaryMiddleViewModel DiaryMiddleViewModel = new DiaryMiddleViewModel();

    FirebaseDatabase database;
    DatabaseReference diaryRef;

    TextView date;
    EditText diaryTitleText;
    EditText diaryContentText;

    int year;
    int month;
    int dayOfMonth;
    String dateStr; //20210519 형식

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        //날짜 받아오기
        Intent dateIntent = getIntent();
        year = dateIntent.getIntExtra("연", 1);
        month = dateIntent.getIntExtra("월", 1);
        dayOfMonth = dateIntent.getIntExtra("일", 1);
        date = findViewById(R.id.writeDate);
        date.setText(DiaryMiddleViewModel.getCalendarDate());
        dateStr = "/" + year + month + dayOfMonth + "/";
        diaryTitleText = (EditText)findViewById(R.id.diaryTitleWriteText);
        diaryContentText = (EditText)findViewById(R.id.diaryContentWriteText);

        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/");  //추후 로그인한 사용자의 아이디로 변경할 것
    }

    //작성 완료 버튼 클릭 시 DB 반영   -> 추후 view가 아닌 다른 패키지로 옮길 예정
    public void processAdd(View view){

        //작성한 일기 제목, 내용
        String diaryTitle = diaryTitleText.getText().toString();
        String diaryContent = diaryContentText.getText().toString();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;

        Diary diary = new Diary(diaryTitle, diaryContent, 1);   //model Diary 객체
        diaryValues = diary.toMap();
        childUpdates.put(dateStr, diaryValues); //diaryValues가 null이면 기존 데이터 삭제됨
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
