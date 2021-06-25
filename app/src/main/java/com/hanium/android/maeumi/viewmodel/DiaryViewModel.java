package com.hanium.android.maeumi.viewmodel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.model.Diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaryViewModel {

    FirebaseDatabase database;
    DatabaseReference diaryRef;
    DiaryMiddleViewModel DiaryMiddleViewModel;

    public static String calendarDate, fireDate;

    public static String title, content, nullDiary;
    public static int emoticonNum;
    public static int maxDay;

    // 조회 - 수정 삭제 / 작성
    public DiaryViewModel() {

    }

    // FireBase, 캘린더 날짜 저장
    public void setDate() {
        fireDate = DiaryMiddleViewModel.getFBDate();
        calendarDate = DiaryMiddleViewModel.getCalendarDate();
        maxDay = DiaryMiddleViewModel.getMaxDay();
        System.out.println("fireDate - "+ fireDate);
        System.out.println("calendarDate - "+ calendarDate);
        System.out.println("Max Day - "+ maxDay);
//        getDiaryFromFB(fireDate);
    }

    //일기 제목, 내용, 이모티콘 번호 조회
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmoticonNum(int emoticonNum) {
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

    public int getEmoticonNum() {
        return this.emoticonNum;
    }

    public String getNullDiary() {
        return this.nullDiary;
    }

    public ArrayList setMonthDiary(){
        ArrayList<Integer> dates = new ArrayList<>();


        return dates;
    }

    // Firebase에서 월별 일기 조회
    public void getMonthDiary(String date) {
        database = FirebaseDatabase.getInstance();

        for (int num = 1; num <= 30; num++) {
            diaryRef = database.getReference("/일기장/아이디/" + date + num);
            System.out.println("5월" + num + " 일 일기조회");

            diaryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Diary value = dataSnapshot.getValue(Diary.class);
                        System.out.println("title- " + value.title);
                        System.out.println("date - " + value.date);
//                        dates.add(value.date);
//                    setTitle(value.title);
//                    setContent(value.content);
//                    setEmoticonNum(value.emoticonNum);
//                    setNullDiary("not Null");
                    } catch (Exception e) {
                        System.out.println("error - " + e);
                        System.out.println("일기 없음");
//                    setNullDiary(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    System.out.println("Failed to read value." + error.toException());
                }
            });

        }
    }
    // 개별 일기 조회
    public void getDiaryFromFB(String date) {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/" + date);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Diary value = dataSnapshot.getValue(Diary.class);
                    System.out.println("title- " + value.title);
                    System.out.println("date - " + value.date);
//                    setTitle(value.title);
//                    setContent(value.content);
//                    setEmoticonNum(value.emoticonNum);
//                    setNullDiary("not Null");
                } catch (Exception e) {
                    System.out.println("error - " + e);
                    System.out.println("일기 없음");
//                    setNullDiary(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    // 일기 작성
    public void diaryWrite(Diary value){
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/");


        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;

        Diary diary = new Diary(value.title, value.content, 1,value.date);   //model Diary 객체
        diaryValues = diary.toMap();
        System.out.println("diaryValue- " + diaryValues);
        childUpdates.put(fireDate, diaryValues); //diaryValues가 null이면 기존 데이터 삭제됨
        diaryRef.updateChildren(childUpdates);
    }
}




