package com.hanium.android.maeumi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.model.Diary;

import java.util.List;


public class DiaryViewModel extends ViewModel {

    FirebaseDatabase database;
    DatabaseReference diaryRef;

    private MutableLiveData<List<Diary>> diary;
    public LiveData<List<Diary>> getDiaryData(){
        if(diary == null){
            diary = new MutableLiveData<List<Diary>>();
            testData();
        }
        return diary;
    }

    private void testData(){
            System.out.println("Test From View Model");
    }

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




