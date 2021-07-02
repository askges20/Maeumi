package com.hanium.android.maeumi.view.diary;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {

    public static LocalDate selectDate;

    //캘린더 상단 년,월 (m월 yyyy)
    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> dayInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate first0fMonth = CalendarUtils.selectDate.withDayOfMonth(1);  //해당 월의 첫번째 일
        int day0fweek = first0fMonth.getDayOfWeek().getValue();     //첫번째 일 (숫자)

        for (int i = 1; i <= 42; i++) {
            if (i <= day0fweek || i > daysInMonth + day0fweek) {    //달력 범위 벗어나는 부분 빈문자열 처리
                dayInMonthArray.add("");
            } else {
                dayInMonthArray.add(String.valueOf(i - day0fweek));
            }
        }
        return dayInMonthArray;
    }

}
