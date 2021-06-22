package com.hanium.android.maeumi.viewmodel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.model.Diary;

public class DiaryViewModel {

    FirebaseDatabase database;
    DatabaseReference diaryRef;
    DiaryMiddleViewModel DiaryMiddleViewModel;

    public static String calendarDate, fireDate;

    public static String title, content, nullDiary;
    public static int emoticonNum;

    // 조회 - 수정 삭제 / 작성
    public DiaryViewModel() {

    }

    // FireBase, 캘린더 날짜 저장
    public void setDate() {
        fireDate = DiaryMiddleViewModel.getFBDate();
        calendarDate = DiaryMiddleViewModel.getCalendarDate();
        getDiaryFromFB(fireDate);
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
    public void setNullDiary(String data){
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

    public String getNullDiary(){ return this.nullDiary;}

    //     Firebase에서 일기 조회
    public void getDiaryFromFB(String date) {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/" + date);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    Diary value = dataSnapshot.getValue(Diary.class);
                    setTitle(value.title);
                    setContent(value.content);
                    setEmoticonNum(value.emoticonNum);
                    setNullDiary("not Null");
                } catch (Exception e) {
                    System.out.println("error - "+ e);
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

}




