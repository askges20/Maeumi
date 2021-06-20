package com.hanium.android.maeumi.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.model.Diary;


public class DiaryViewModel extends ViewModel {

    FirebaseDatabase database;
    DatabaseReference diaryRef;

    //LiveData 생성
    //private MutableLiveData<Diary> currentDiary = new MutableLiveData<Diary>();

    //일반 Diary 객체
    private Diary currentDiary = new Diary();

    public Diary getSelectedDiary(int year, int month, int day){
        currentDiary = null;

        //firebase에서 해당 날짜 일기 검색
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/"+year+month+day);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    currentDiary = dataSnapshot.getValue(Diary.class);
                    System.out.println(year+""+month+""+day +" 일기조회");
                } catch (Exception e){  //해당 날짜 일기가 없는 경우
                    System.out.println("일기 없음");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value."+ error.toException());
            }
        });

        return currentDiary;    //찾은 일기 리턴
    }



//    private MutableLiveData<Class<Diary>> diary;
//    public LiveData<Class<Diary>> getDiaryData(){
//        if(diary == null){
//            diary = new MutableLiveData<Class<Diary>>();
//            System.out.println("1 - "+diary);
//            testData();
//        }
//        System.out.println("2 - "+diary);
//        return diary;
//    }

    //private final MutableLiveData diary = new MutableLiveData<>();


    /*
    public LiveData getLiveDataTest(){
        return diary;
    }

    public void setLiveDataTest(String str){
        diary.setValue(str);
        System.out.println("setLiveDataTest --"+diary.getValue());
    }

    private void testData(){
            System.out.println("Test From View Model");
            System.out.println("3 - "+diary);
          System.out.println("Test From View Model");
    }
     */

//     Firebase에서 일기 조회
    private void getData(int year,int month,int dayOfMonth){
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/"+year+month+dayOfMonth);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    Diary value = dataSnapshot.getValue(Diary.class);
                    System.out.println(year+""+month+""+dayOfMonth +" 일기조회");
                    System.out.println("Title: " + value.title);
                    System.out.println("Content: " + value.content);
                    System.out.println("EmoticonNum: " + value.emoticonNum);
                }catch (Exception e){
                    System.out.println("일기 없음");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value."+ error.toException());
            }
        });
    }

}




