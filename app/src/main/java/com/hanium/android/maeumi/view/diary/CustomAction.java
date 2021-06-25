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
import com.hanium.android.maeumi.viewmodel.DiaryMiddleViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomAction extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectDate;

    DiaryMiddleViewModel DiaryMiddleViewModel = new DiaryMiddleViewModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        iniwigets();
        selectDate = LocalDate.now();
        System.out.println("selectDate : " + selectDate);
        setMonthView();
        // 월별 일기 조회 후 점 찍기
        DiaryMiddleViewModel.setMaxDay(selectDate);
    }


    private void iniwigets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> dayInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        System.out.println("yearMonth : " + yearMonth);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate first0fMonth = selectDate.withDayOfMonth(1);
        System.out.println("firstOfMonth : " + first0fMonth);
        int day0fweek = first0fMonth.getDayOfWeek().getValue();
        System.out.println("dayOfweek" + day0fweek);

        for (int i = 1; i <= 42; i++) {
            if (i <= day0fweek || i > daysInMonth + day0fweek) {
                dayInMonthArray.add("");
            } else {
                dayInMonthArray.add(String.valueOf(i - day0fweek));
            }
        }
        return dayInMonthArray;
    }


    public void previousMonthAction(View view) {
        selectDate = selectDate.minusMonths(1);
        // 월별 일기 조회 후 점 찍기
        System.out.println("previous Month - " + selectDate);
        DiaryMiddleViewModel.setMaxDay(selectDate);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectDate = selectDate.plusMonths(1);
        // 월별 일기 조회 후 점 찍기
        System.out.println("next Month- " + selectDate);
        DiaryMiddleViewModel.setMaxDay(selectDate);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        // 클릭 날짜 표시 [x]
        // ViewModel에 날짜 저장 [o]
        DiaryMiddleViewModel.setDate(selectDate, dayText);
        String message = "Selected Date" + dayText + "" + monthYearFromDate(selectDate);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void diaryContent(View view) {
        Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
        startActivity(intent);
        System.out.println("Move To Diary Content");
    }

    public void diaryWrite(View view) {
        Intent intent = new Intent(getApplicationContext(), DiaryWrite.class);
        startActivity(intent);
        System.out.println("Move To Diary Write");
    }
}

