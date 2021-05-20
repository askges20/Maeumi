package com.hanium.android.maeumi.view.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hanium.android.maeumi.R;

public class DiaryModify extends Activity {
    TextView date;
    int year;
    int month;
    int dayOfMonth;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_modify);

        Intent dateIntent = getIntent();
        year = dateIntent.getIntExtra("연", 1);
        month = dateIntent.getIntExtra("월", 1);
        dayOfMonth = dateIntent.getIntExtra("일", 1);;

        date = findViewById(R.id.modifyDate);
        date.setText(year + "/" + month + "/" + dayOfMonth);
    }

    public void processModify(View view){   //수정 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT);
        /*
        수정한 내용 반영
        */
        toastView.show();
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
}
