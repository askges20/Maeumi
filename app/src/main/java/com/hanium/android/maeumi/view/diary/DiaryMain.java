package com.hanium.android.maeumi.view.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.hanium.android.maeumi.view.diary.CalendarUtils.daysInMonthArray;
import static com.hanium.android.maeumi.view.diary.CalendarUtils.monthYearFromDate;

public class DiaryMain extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
//    private LocalDate selectDate;

    DiaryViewModel DiaryViewModel = new DiaryViewModel(this);

    public static ArrayList<String> diaryDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        iniwigets();
        CalendarUtils.selectDate = LocalDate.now();
        DiaryViewModel.setCompareMonth(CalendarUtils.selectDate);

        setMonthView();
    }

    public void setDates(ArrayList<String> dates) {
        this.diaryDates = dates;
        System.out.println("월별 일기 내역 " + this.diaryDates);
    }

    //activity_custom.xml 레이아웃 요소 연결
    private void iniwigets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

//    //캘린더 상단 년,월 (m월 yyyy)
//    public static String monthYearFromDate(LocalDate date) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
//        return date.format(formatter);
//    }

    //캘린더 새로고침
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectDate));
        ArrayList<String> daysInMonth = daysInMonthArray(CalendarUtils.selectDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    //선택한 날짜의 월 구하기
//    public static ArrayList<String> daysInMonthArray(LocalDate date) {
//        ArrayList<String> dayInMonthArray = new ArrayList<>();
//        YearMonth yearMonth = YearMonth.from(date);
//
//        int daysInMonth = yearMonth.lengthOfMonth();
//
//        LocalDate first0fMonth = CalendarUtils.selectDate.withDayOfMonth(1);  //해당 월의 첫번째 일
//        int day0fweek = first0fMonth.getDayOfWeek().getValue();     //첫번째 일 (숫자)
//
//        for (int i = 1; i <= 42; i++) {
//            if (i <= day0fweek || i > daysInMonth + day0fweek) {    //달력 범위 벗어나는 부분 빈문자열 처리
//                dayInMonthArray.add("");
//            } else {
//                dayInMonthArray.add(String.valueOf(i - day0fweek));
//            }
//        }
//        return dayInMonthArray;
//    }

    //이전 달 캘린더로 이동
    public void previousMonthAction(View view) {
        CalendarUtils.selectDate = CalendarUtils.selectDate.minusMonths(1);
        DiaryViewModel.setChangeCompareMonth(CalendarUtils.selectDate);

        setMonthView();
    }

    //다음 달 캘린더로 이동
    public void nextMonthAction(View view) {
        CalendarUtils.selectDate = CalendarUtils.selectDate.plusMonths(1);
        DiaryViewModel.setChangeCompareMonth(CalendarUtils.selectDate);

        setMonthView();
    }

    //캘린더에서 날짜 선택했을 때 이벤트 리스너
    @Override
    public void onItemClick(int position, String dayText) {
        // 클릭 날짜 표시 [x]
        DiaryViewModel.setDate(CalendarUtils.selectDate, dayText);
        String message = "선택한 날짜 : " + dayText + " " + monthYearFromDate(CalendarUtils.selectDate);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //일기 내용 페이지로 이동
    //추후 캘린더에 일기 작성 여부에 따른 점 표시 구현 -> (일기가 있는 날이면) 날짜 선택 시 자동으로 일기 내용 페이지로 이동시킬 예정
    public void diaryContent(View view) {
        Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
        startActivity(intent);
        System.out.println("Move To Diary Content");
    }

    //일기 작성 페이지로 이동
    public void diaryWrite(View view) {
        Intent intent = new Intent(getApplicationContext(), DiaryWrite.class);
        startActivity(intent);
        System.out.println("Move To Diary Write");
    }
}

