package com.hanium.android.maeumi.view.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.CalendarAdapter;
import com.hanium.android.maeumi.model.DiaryModel;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.hanium.android.maeumi.view.diary.CalendarUtils.daysInMonthArray;
import static com.hanium.android.maeumi.view.diary.CalendarUtils.monthYearFromDate;

public class DiaryMain extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    Button diaryButton;

    DiaryModel DiaryModel = new DiaryModel(this);
    CalendarAdapter calendarAdapter;

    public static ArrayList<String> diaryDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_main);

        iniwigets();
        CalendarUtils.selectDate = LocalDate.now(); //CalendarUtils에 오늘 날짜 전달
        DiaryModel.setCompareMonth(CalendarUtils.selectDate);

        setMonthView(); //캘린더 표시
    }

    public void goToBack(View view){
        finish();
    }

    public void setDates(ArrayList<String> dates) {
        System.out.println("메인에서 실행 : setDates " + dates);
        this.diaryDates = dates;
        calendarAdapter.SetDates(this.diaryDates);
        System.out.println("setDates에서 setMonthView 실행");
        setMonthView();
    }

    //activity_diary_main.xml.xml 레이아웃 요소 연결
    private void iniwigets() {
        System.out.println("메인에서 실행 : 다이어리 xml 레이아웃 연결");
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        diaryButton = findViewById(R.id.diaryWriteBtn);
    }

    //캘린더 새로고침
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectDate)); //년, 월 표시
        ArrayList<String> daysInMonth = daysInMonthArray(CalendarUtils.selectDate); //캘린더 날짜 배열 세팅
        System.out.println("메인에서 실행 : 캘린더 날짜 배열 : " + daysInMonth);

        calendarAdapter = new CalendarAdapter(daysInMonth,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);   //레이아웃 매니저 설정
        calendarRecyclerView.setAdapter(calendarAdapter);   //캘린더 어댑터 등록
        System.out.println("메인에서 실행 : 캘린더 어댑터 등록");

    }

    //이전 달 캘린더로 이동
    public void previousMonthAction(View view) {
        System.out.println("메인에서 실행 : 이전 달로 이동");
        CalendarUtils.selectDate = CalendarUtils.selectDate.minusMonths(1);
        DiaryModel.setChangeCompareMonth(CalendarUtils.selectDate);
        setMonthView(); //화면에 표시
    }

    //다음 달 캘린더로 이동
    public void nextMonthAction(View view) {
        System.out.println("메인에서 실행 : 다음 달로 이동");
        CalendarUtils.selectDate = CalendarUtils.selectDate.plusMonths(1);
        DiaryModel.setChangeCompareMonth(CalendarUtils.selectDate);
        setMonthView(); //화면에 표시
    }

    //캘린더에서 날짜 선택했을 때 이벤트 리스너
    @Override
    public void onItemClick(int position, String dayText) {
        DiaryModel.setDate(CalendarUtils.selectDate,dayText);
        if(dayText != ""){
            diaryButton.setText(dayText + "일 일기 작성하기");
        }else{
            diaryButton.setText("날짜를 선택하세요");
        }
        Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
        startActivity(intent);
    }

    //일기 작성 페이지로 이동
    public void diaryWrite(View view) {
        Intent intent = new Intent(getApplicationContext(), DiaryWrite.class);
        startActivity(intent);
    }
}

