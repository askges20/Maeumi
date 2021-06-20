package com.hanium.android.maeumi.view.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.viewmodel.DiaryMiddleViewModel;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.util.Calendar;

public class DiaryCalendar extends AppCompatActivity {
    CalendarView calendarView;
    int year;
    int month;
    int dayOfMonth;

    DiaryMiddleViewModel DiaryMiddleViewModel = new DiaryMiddleViewModel();
    DiaryViewModel DiaryViewModel = new DiaryViewModel();

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
                DiaryCalendar.this.year = year;
                DiaryCalendar.this.month = month + 1;
                DiaryCalendar.this.dayOfMonth = dayOfMonth;
                String date = year + "/" + (month+1) + "/" + (dayOfMonth);
                Toast.makeText(DiaryCalendar.this, date, Toast.LENGTH_SHORT).show();//선택한 날짜 toast
                DiaryMiddleViewModel.setCalendarDate(year,month,dayOfMonth);
            }
        });
    }

    public void goToDiaryContent(View view){
        Intent intent = new Intent(DiaryCalendar.this, DiaryContent.class);
        intent.putExtra("연", year);
        intent.putExtra("월", month);
        intent.putExtra("일", dayOfMonth);
        startActivity(intent);
        System.out.println("Move To Diary Content");
    }

    public void goToDiaryWrite(View view){
//        Intent intent = new Intent(DiaryCalendar.this, DiaryWrite.class);
        Intent intent = new Intent(getApplicationContext(),DiaryWrite.class);
        intent.putExtra("연", year);
        intent.putExtra("월", month);
        intent.putExtra("일", dayOfMonth);
        startActivity(intent);
        System.out.println("Move To Diary Write");
    }

    public void goToCalendarTest(View view){
        Intent intent = new Intent(DiaryCalendar.this, DiaryCalendarTest.class);
        startActivity(intent);
        System.out.println("Move To Diary Test");
    }

}
