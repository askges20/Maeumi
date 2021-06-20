package com.hanium.android.maeumi.viewmodel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.model.Diary;
import com.hanium.android.maeumi.viewmodel.DiaryMiddleViewModel;
import com.hanium.android.maeumi.model.Diary;

public class DiaryViewModel  {

    FirebaseDatabase database;
    DatabaseReference diaryRef;
    DiaryMiddleViewModel DiaryMiddleViewModel;

    public static String calendarDate;
    public static String fireDate;


    public DiaryViewModel(){

    }

    // FB 날짜 저장
    public void setFireDate(){
        fireDate = DiaryMiddleViewModel.getFBDate();
        System.out.println("setDate-" + fireDate);
    }

    // 캘린더 날짜 저장
    public void setCalendarDate(){
        calendarDate = DiaryMiddleViewModel.getCalendarDate();
        System.out.println("setDate-" + calendarDate);
    }
    public String getCalendarDate(){
        return this.calendarDate;
    }

//     Firebase에서 일기 조회
//    private void getDiaryData(String date){
//        System.out.println("getDiaryDate-"+date);
//        database = FirebaseDatabase.getInstance();
//        diaryRef = database.getReference("/일기장/아이디/"+date);
//
//        diaryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                try{
//                    Diary value = dataSnapshot.getValue(Diary.class);
//                    System.out.println(date+"ViewModel 일기조회");
//                    System.out.println("Title: " + value.title);
//                    System.out.println("Content: " + value.content);
//                    System.out.println("EmoticonNum: " + value.emoticonNum);
//                }catch (Exception e){
//                    System.out.println("일기 없음");
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                System.out.println("Failed to read value."+ error.toException());
//            }
//        });
//    }

}




