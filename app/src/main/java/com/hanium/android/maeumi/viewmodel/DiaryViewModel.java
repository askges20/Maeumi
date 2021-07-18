package com.hanium.android.maeumi.viewmodel;

import android.graphics.Bitmap;

import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.model.Diary;
import com.hanium.android.maeumi.model.DiaryModel;
import com.hanium.android.maeumi.view.diary.DiaryMain;

import java.time.LocalDate;
import java.util.ArrayList;

public class DiaryViewModel {

    DiaryMain DiaryMain;
    DiaryModel DiaryModel = new DiaryModel(this);

    public static String calendarDate, fireDate;

    public static String title, content, nullDiary,emoticonNum,saveId;

    public static Bitmap imgName;

    public static String day, year, month, oneTimeDate, oneTimeMonth, compareMonth;

    public static ArrayList<String> dates = new ArrayList<>();

    public DiaryViewModel() {

    }

    public DiaryViewModel(DiaryMain custom) {
        this.DiaryMain = custom;
    }

    // Intent에서 생성된 날짜를 FireBase, 캘린더 날짜로 바꾸고 저장
    public void setDate(LocalDate monthYear, String date) {
        this.oneTimeDate = monthYear.toString();
        this.year = this.oneTimeDate.substring(0, 4);
        this.month = this.oneTimeDate.substring(5, 7);
        this.day = dayPlusZero(date);
        this.fireDate = "/" + this.year + this.month + this.day + "/";
        if (date == "") {
            this.calendarDate = null;
        } else {
            this.calendarDate = this.year + "년 " + this.month + "월 " + this.day + "일";
        }

        // ViewModel에 날짜 저장
        DiaryModel.setDate();
    }

    public void setCompareMonth(LocalDate selectDate) {
        this.oneTimeMonth = selectDate.toString();
        this.compareMonth = this.oneTimeMonth.replace("-", "");
        this.compareMonth = this.compareMonth.substring(0, 6);

        DiaryModel.setCompareDate(this.compareMonth);
    }

    public void setChangeCompareMonth(LocalDate selectDate) {
        this.oneTimeMonth = selectDate.toString();
        this.compareMonth = this.oneTimeMonth.replace("-", "");
        this.compareMonth = this.compareMonth.substring(0, 6);

        DiaryModel.setChangeCompareDate(this.compareMonth);
    }

    public void setMonthDiaryDates() {
        this.dates = DiaryModel.getMonthDiaryDates();
        DiaryMain.setDates(this.dates);
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

    // 일기 제목, 내용, 이모티콘 번호 불러오기
    public void setDiaryData() {
        resetDiary();
        this.nullDiary = DiaryModel.getNullDiary();
        if (this.nullDiary != null) {
            this.title = DiaryModel.getTitle();
            this.content = DiaryModel.getContent();
            this.emoticonNum = DiaryModel.getEmoticonNum();
        }
    }
    public void resetDiary(){
        this.title = null;
        this.content = null;
        this.emoticonNum = null;
    }

    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public String getEmoticonNum() {
        return emoticonNum;
    }

    public String getNullDiary() {
        return nullDiary;
    }


    // 일기 작성 & 수정
    public void diaryWrite(String title, String content,String diaryEmoticon) {
        Diary value = new Diary(title, content, diaryEmoticon, this.day);
        DiaryModel.diaryWrite(value);
    }

    // 일기 삭제
    public void deleteDiary() {
        DiaryModel.deleteDiary();
    }


    // 이미지 비트맵 저장, 전달
    public void setImgName(Bitmap imgBitmap){
        this.imgName = imgBitmap;

        DiaryModel.setImgName();
    }

    // 이미지 비트맵 저장, 전달
    public Bitmap getImgName(){
        return imgName;
    }

    // 아이디 + 날짜로 이미지 조회 id 생성
    public String getFireImgName(){
        this.saveId = LoginUser.getInstance().getUid() +fireDate;
        return saveId;
    }
}