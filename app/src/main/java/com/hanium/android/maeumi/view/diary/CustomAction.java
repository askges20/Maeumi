package com.hanium.android.maeumi.view.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomAction extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        iniwigets();
        selectedDate = LacalDate.now();
        setMonthView();
    }


    private void iniwigets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> dayInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate first0fMonth = selectDate.withDayOfMonth(1);
        int day0fweek = first0fMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= day0fweek || i > daysInMonth + day0fweek) {
                dayInMonthArray.add("");
            } else {
                dayInMonthArray.add(String.valueOf(i + day0fweek));
            }
        }
        return daysInMonthArray;
    }

    public void previousMonthAction (View view){
        selectDate = selectDate.minusMonths(1);
        setMonthView();
    }
    public void nextMonthAction (View view){
        selectDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText.equals("")) {
            String message = "Selected Date" + dayText + "" + monthyearfromDate(selectDate);
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }

    }
}


