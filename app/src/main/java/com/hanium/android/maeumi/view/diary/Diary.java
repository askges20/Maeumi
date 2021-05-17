package com.hanium.android.maeumi.view.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hanium.android.maeumi.R;

import java.util.Calendar;

public class Diary extends Activity {
    CalendarView calendarView;
    int year;
    int month;
    int dayOfMonth;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_main);

        //날짜 디폴트값 = 현재 날짜
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        dayOfMonth = calendar.get(Calendar.DATE);

        calendarView = (CalendarView) findViewById(R.id.myCalendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Diary.this.year = year;
                Diary.this.month = month + 1;
                Diary.this.dayOfMonth = dayOfMonth;
                String date = year + "/" + (month+1) + "/" + (dayOfMonth);
                Toast.makeText(Diary.this, date, Toast.LENGTH_SHORT).show();    //선택한 날짜 toast
            }
        });
    }

    public void goToDiaryContent(View view){
        Intent intent = new Intent(Diary.this, DiaryContent.class);
        intent.putExtra("연", year);
        intent.putExtra("월", month);
        intent.putExtra("일", dayOfMonth);
        startActivity(intent);
        System.out.println("Move To Diary Content");
    }

    public void goToDiaryWrite(View view){
        Intent intent = new Intent(Diary.this, DiaryWrite.class);
        intent.putExtra("연", year);
        intent.putExtra("월", month);
        intent.putExtra("일", dayOfMonth);
        startActivity(intent);
        System.out.println("Move To Diary Write");
    }

}
