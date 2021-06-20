package com.hanium.android.maeumi.viewmodel;

public class DiaryMiddleViewModel {


    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    public static String calendarDate, fireDate;

    public static String title, content;
    public static int emoticonNum;

    public String year, month, day;

    public String testDate;

    public DiaryMiddleViewModel() {

    }

    // Intent에서 생성된 날짜를 FireBase, 캘린더 날짜로 바꾸기
    public void setDate(int year, int month, int day) {
        month = month + 1;
        this.year = Integer.toString(year);
        this.month = Integer.toString(month);
        this.day = Integer.toString(day);

        this.fireDate = this.year + this.month + this.day;
        System.out.println("setFireDate-" + this.fireDate);

        this.calendarDate = this.year + "년 " + this.month + "월 " + this.day + "일";
        System.out.println("setCalendarDate-" + this.calendarDate);

        DiaryViewModel.setDate();
    }

    // FB 날짜 불러오기
    public static String getFBDate() {
        return fireDate;
    }

    // 캘린더 날짜 불러오기
    public static String getCalendarDate() {
        return calendarDate;
    }


    //일기 제목, 내용, 이모티콘 번호 조회
    public String getContentFromViewModel() {
//        this.title = DiaryViewModel.getTitle();
        return title;
    }

}
//title = DiaryViewModel.getTitle();
//        content = DiaryViewModel.getContent();
//        emoticonNum = DiaryViewModel.getEmoticonNum();