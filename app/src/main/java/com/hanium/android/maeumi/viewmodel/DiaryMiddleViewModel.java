package com.hanium.android.maeumi.viewmodel;

import com.hanium.android.maeumi.model.Diary;
import com.hanium.android.maeumi.view.diary.CustomAction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class DiaryMiddleViewModel {

    CustomAction customAction;
    DiaryViewModel DiaryViewModel = new DiaryViewModel(this);

    public static String calendarDate, fireDate;

    public static String title, content, nullDiary;
    public static int emoticonNum;

    public static String day, year, month, oneTimeDate, oneTimeMonth,compareMonth;

    public static ArrayList<String> dates = new ArrayList<>();

    public DiaryMiddleViewModel() {

    }

    public DiaryMiddleViewModel(CustomAction custom){
        this.customAction = custom;
    }

    // Intent에서 생성된 날짜를 FireBase, 캘린더 날짜로 바꾸고 저장
    public void setDate(LocalDate monthYear, String date) {
        this.oneTimeDate = monthYear.toString();
        this.year = this.oneTimeDate.substring(0, 4);
        this.month = this.oneTimeDate.substring(5, 7);
        this.day = dayPlusZero(date);
        this.fireDate = "/"+ this.year + this.month + this.day+"/";
        if(date == ""){
            this.calendarDate = null;
        }else{
            this.calendarDate = this.year + "년 " + this.month + "월 " + this.day + "일";
        }

        // ViewModel에 날짜 저장
        DiaryViewModel.setDate();
    }

    public void setCompareMonth(LocalDate selectDate){
        System.out.println("selectDate : " + selectDate);
        this.oneTimeMonth = selectDate.toString();
        this.compareMonth = this.oneTimeMonth.replace("-","");
        this.compareMonth = this.compareMonth.substring(0,6);

        DiaryViewModel.setCompareDate(this.compareMonth);
    }
    public void setChangeCompareMonth(LocalDate selectDate){
        System.out.println("selectDate : " + selectDate);
        this.oneTimeMonth = selectDate.toString();
        this.compareMonth = this.oneTimeMonth.replace("-","");
        this.compareMonth = this.compareMonth.substring(0,6);

        DiaryViewModel.setChangeCompareDate(this.compareMonth);
    }
    public void setTestDiaryDate(String date){
        if(date == null){
            System.out.println("DATE IS NULL");
        }else{
            System.out.println("date - "+date);
        }
    }

    public void setHelloMonthDiary(){
        this.dates = DiaryViewModel.getHelloMonthDiary();
        customAction.setDates(this.dates);
        System.out.println("hello - "+ this.dates);
    }
    public ArrayList getHelloMonthDiary(){
        return DiaryViewModel.getHelloMonthDiary();
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
//    public static int getMaxDay() {
//        return maxDay;
//    }

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

    
    // 일기 작성 & 수정
    public void diaryWrite(String title, String content){
        Diary value = new Diary(title,content,1,this.day);
        DiaryViewModel.diaryWrite(value);
    }

    // 일기 삭제
    public void deleteDiary(){
        DiaryViewModel.deleteDiary();
    }

}