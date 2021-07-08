package com.hanium.android.maeumi.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class DiaryModel {

    FirebaseDatabase database;
    DatabaseReference diaryRef;
    DiaryViewModel DiaryViewModel;
    FirebaseAuth firebaseAuth;

    public static String calendarDate, fireDate, compareMonth, monthlyDate, userId;

    public static String title, content, nullDiary,emoticonNum;

    public static ArrayList<String> dates = new ArrayList<>();

    public DiaryModel(DiaryViewModel middle) {
        this.DiaryViewModel = middle;
    }

    // FireBase, 캘린더 날짜 저장
    public void setDate() {
        fireDate = DiaryViewModel.getFBDate();
        calendarDate = DiaryViewModel.getCalendarDate();
        getDiaryFromFB(fireDate);
    }

    public void setCompareDate(String date) {
        this.compareMonth = date;

        getUserId();
        getMonthDiary();
    }

    public void setChangeCompareDate(String date) {
        dates.clear();
        this.compareMonth = date;

        getUserId();
        getMonthDiary();
    }

    //   유저 uid 조회, 저장
    public void getUserId(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
    }

    //일기 제목, 내용, 이모티콘 번호 조회
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmoticonNum(String emoticonNum) {
        this.emoticonNum = emoticonNum;
    }

    // Null Diary
    public void setNullDiary(String data) {
        this.nullDiary = data;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getEmoticonNum() {
        return this.emoticonNum;
    }

    public String getNullDiary() {
        return this.nullDiary;
    }

    public ArrayList getMonthDiaryDates() {
        return dates;
    }

    public void setMonthDiaryDates(String date){

        dates.add(date);
    }

    // Firebase에서 월별 일기 조회
    public void getMonthDiary() {
        System.out.println("month - "+ userId);
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/"+userId + "/");

        diaryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dateSnap : dataSnapshot.getChildren()) { //하위 구조 (작성일자)
                    if (compareMonth.equals(dateSnap.getKey().substring(0, 6))) {
                        Diary testValue = dateSnap.getValue(Diary.class);
                        monthlyDate = testValue.date + testValue.emoticonNum;
//                        dates.add(monthlyDate);
                        setMonthDiaryDates(monthlyDate);
                    }
                    for (DataSnapshot snap : dateSnap.getChildren()) { //하위 구조 (게시글)
                    }
                }
                DiaryViewModel.setMonthDiaryDates();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    // 개별 일기 조회
    public void getDiaryFromFB(String date) {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/"+userId + "/"+ date);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Diary value = dataSnapshot.getValue(Diary.class);
                    setTitle(value.title);
                    setContent(value.content);
                    setEmoticonNum(value.emoticonNum);
                    setNullDiary("Diary Not Null");
                } catch (Exception e) {
                    setNullDiary(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    // 일기 작성 & 수정
    public void diaryWrite(Diary value) {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/"+userId + "/");


        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;

        Diary diary = new Diary(value.title, value.content, value.emoticonNum, value.date);   //model Diary 객체
        diaryValues = diary.toMap();
        childUpdates.put(fireDate, diaryValues); //diaryValues가 null이면 기존 데이터 삭제됨
        diaryRef.updateChildren(childUpdates);
    }

    // 일기 삭제
    public void deleteDiary() {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/"+userId + "/");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(fireDate, null); //dnull이라 기존 데이터 삭제됨
        diaryRef.updateChildren(childUpdates);
    }
}




