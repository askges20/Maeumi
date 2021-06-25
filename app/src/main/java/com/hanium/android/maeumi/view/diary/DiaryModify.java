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

    String diaryCalDate;
    String diaryTitle, diaryContent;
    int diaryEmoticonNum;

    TextView dateText;
    EditText titleText;
    EditText contentText;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_modify);

        diaryCalDate = DiaryMiddleViewModel.getCalendarDate();

        dateText = findViewById(R.id.modifyDate);
        titleText = findViewById(R.id.diaryTitleModifyText);
        contentText = findViewById(R.id.diaryContentModifyText);

        diaryTitle = DiaryMiddleViewModel.getTitle();
        diaryContent = DiaryMiddleViewModel.getContent();
        diaryEmoticonNum = DiaryMiddleViewModel.getEmoticonNum();

        dateText.setText(diaryCalDate);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);
        dateText.setText(diaryCalDate);
    }

    public void processModify(View view){   //수정 완료 버튼 클릭 시
        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        DiaryMiddleViewModel.diaryWrite(diaryTitle,diaryContent);

        Toast toastView = Toast.makeText(DiaryModify.this, "수정 완료", Toast.LENGTH_SHORT);
        toastView.show();

        // 다시 일기장 메인 화면으로 이동
        Intent intent = new Intent(DiaryModify.this, CustomAction.class);
        startActivity(intent);
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
}