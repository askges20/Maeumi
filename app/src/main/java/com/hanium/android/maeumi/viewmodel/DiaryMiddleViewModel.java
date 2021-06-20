package com.hanium.android.maeumi.viewmodel;

public class DiaryMiddleViewModel {


    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    public static String fireDate;
    public static String calendarDate;

    public String year;
    public String month;
    public String day;

    public String testDate;

    public DiaryMiddleViewModel() {

    }

    // Intent 날짜 FB 조회용 날짜로 바꾸기
    public void setFBDate(int year, int month, int day) {
        month = month + 1;
        this.year = Integer.toString(year);
        this.month = Integer.toString(month);
        this.day = Integer.toString(day);
        this.fireDate = this.year + this.month + this.day;
        this.callFireDate();
    }

    // Intetn 날짜 캘린더용 날짜로 바꾸기
    public void setCalendarDate(int year, int month, int day) {
        month = month + 1;
        this.year = Integer.toString(year);
        this.month = Integer.toString(month);
        this.day = Integer.toString(day);
        this.calendarDate = this.year + "년 " + this.month + "월 " + this.day + "일";
        this.callCalendarDate();
    }
    
    // FB 날짜 불러오기
    public static String getFBDate() {
        return fireDate;
    }

    // 캘린더 날짜 불러오기
    public static String getCalendarDate() {
        return calendarDate;
    }

    // FB 조회 날짜 불러오기
    public void callFireDate() {
        if (this.fireDate != null) {
            System.out.println("setDiaryDate-" + this.fireDate);
            DiaryViewModel.setFireDate();
        } else {
            System.out.println("Date is Null");
        }
    }

    // 일기 조회 날짜 불러오기
    public void callCalendarDate() {
        if (this.calendarDate != null) {
            System.out.println("setDiaryDate-" + this.calendarDate);
            DiaryViewModel.setCalendarDate();
        } else {
            System.out.println("Date is Null");
        }
    }

    // 테스트
    public void getFromViewModel() {
        testDate = DiaryViewModel.getCalendarDate();
        System.out.println("testFrom" + testDate);
    }

}
