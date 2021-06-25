package com.hanium.android.maeumi.viewmodel;

import java.time.LocalDate;
import java.time.YearMonth;

public class DiaryMiddleViewModel {


    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    public static String calendarDate, fireDate;

    public static String title, content, nullDiary;
    public static int emoticonNum;

    public static String day, year, month, oneTimeDate;
    public static int maxDay;

    public DiaryMiddleViewModel() {

    }

    // Intent에서 생성된 날짜를 FireBase, 캘린더 날짜로 바꾸고 저장
    public void setDate(LocalDate monthYear, String date) {
        this.oneTimeDate = monthYear.toString();
        this.year = this.oneTimeDate.substring(0, 4);
        this.month = this.oneTimeDate.substring(5, 7);
        this.day = dayPlusZero(date);
        this.fireDate = this.year + this.month + this.day;
        this.calendarDate = this.year + "년 " + this.month + "월 " + this.day + "일";

        // ViewModel에 날짜 저장
        DiaryViewModel.setDate();
    }
    public void setMaxDay(LocalDate monthYear){
        YearMonth yearMonth = YearMonth.from(monthYear);
        this.maxDay = yearMonth.lengthOfMonth();
    }

    public String dayPlusZero(String date) {
        if (date.length() < 2) {
            date = "0" + date;
        }
        return date;
    }
    // FB 날짜 불러오기
    public static String getFBDate() {
        return fireDate;
    }

    // 캘린더 날짜 불러오기
    public static String getCalendarDate() {
        return calendarDate;
    }
    // 최대날짜 구하기
    public static int getMaxDay(){
        return maxDay;
    }

    // 일기내용 클릭 시 일기 조회
    public void getMonthDiary() {
        setDiaryData();

    }

    // 일기 제목, 내용, 이모티콘 번호 불러오기
    public void setDiaryData() {
        this.nullDiary = DiaryViewModel.getNullDiary();
        if (this.nullDiary != null) {
            System.out.println(this.nullDiary);
            this.title = DiaryViewModel.getTitle();
            this.content = DiaryViewModel.getContent();
            this.emoticonNum = DiaryViewModel.getEmoticonNum();
        }
    }

    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public int getEmoticonNum() {
        return emoticonNum;
    }

    public String getNullDiary() {
        return nullDiary;
    }

    // 일기 목록 초기화
    public void clearDiary() {
        this.title = null;
        this.content = null;
    }

}