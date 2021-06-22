package com.hanium.android.maeumi.viewmodel;

public class DiaryMiddleViewModel {


    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    public static String calendarDate, fireDate;

    public static String title, content;
    public static int emoticonNum;

    public String year, month, day;

    public static String nullDiary;

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

    // 일기내용 클릭 시 일기 조회
    public void getDiary() {
        setDiaryData();

    }

    // 일기 제목, 내용, 이모티콘 번호 불러오기
    public void setDiaryData() {
        this.nullDiary = DiaryViewModel.getNullDiary();
        System.out.println("nullDiary -- "+ this.nullDiary);
        if (this.nullDiary != null) {
            System.out.println(this.nullDiary);
            this.title = DiaryViewModel.getTitle();
            this.content = DiaryViewModel.getContent();
            this.emoticonNum = DiaryViewModel.getEmoticonNum();
        } else {
            this.nullDiary = null;
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