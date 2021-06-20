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
import com.hanium.android.maeumi.viewmodel.DiaryMiddleViewModel;

import java.util.HashMap;
import java.util.Map;

public class DiaryModify extends Activity {

    DiaryMiddleViewModel DiaryMiddleViewModel = new DiaryMiddleViewModel();
    String testCalDate, testFireDate;

    FirebaseDatabase database;
    DatabaseReference diaryRef;

    TextView dateText;
    EditText titleText;
    EditText contentText;

    int year;
    int month;
    int dayOfMonth;
    String dateStr;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_modify);

        testCalDate = DiaryMiddleViewModel.getCalendarDate();
        testFireDate = DiaryMiddleViewModel.getFBDate();

        dateText = findViewById(R.id.modifyDate);
        titleText = findViewById(R.id.diaryTitleModifyText);
        contentText = findViewById(R.id.diaryContentModifyText);

        /*내용 페이지에서 데이터 받아오기*/
        Intent intent = getIntent();
        year = intent.getIntExtra("연", 1);
        month = intent.getIntExtra("월", 1);
        dayOfMonth = intent.getIntExtra("일", 1);
        titleText.setText(intent.getStringExtra("제목"));
        contentText.setText(intent.getStringExtra("내용"));

//        dateText.setText(year + "/" + month + "/" + dayOfMonth);
//        dateStr = "/" + year + month + dayOfMonth + "/";
        dateText.setText(testCalDate);

        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/");  //추후 로그인한 사용자의 아이디로 변경할 것
    }

    public void processModify(View view){   //수정 완료 버튼 클릭 시
        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;

        Diary diary = new Diary(diaryTitle, diaryContent, 1);   //model Diary 객체
        diaryValues = diary.toMap();
        childUpdates.put(dateStr, diaryValues); //diaryValues가 null이면 기존 데이터 삭제됨
        diaryRef.updateChildren(childUpdates);

        Toast toastView = Toast.makeText(DiaryModify.this, "수정 완료", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
}
