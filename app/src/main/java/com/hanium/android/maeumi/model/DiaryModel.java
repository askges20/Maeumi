package com.hanium.android.maeumi.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaryModel {

    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference diaryRef;
    DiaryViewModel DiaryViewModel;
    LoginUser loginUser = LoginUser.getInstance();

    public static String calendarDate, fireDate, compareMonth, monthlyDate, saveId;

    public static String title, content, nullDiary, emoticonNum;

    public static Bitmap imgName;

    public static ArrayList<String> dates = new ArrayList<>();

    public DiaryModel(DiaryViewModel middle) {
        this.DiaryViewModel = middle;
    }

    // FireBase, 캘린더 날짜 저장
    public void setDate() {
        fireDate = DiaryViewModel.getFBDate();
        calendarDate = DiaryViewModel.getCalendarDate();
        saveId = LoginUser.getInstance().getUid() + fireDate;
        getDiaryFromFB(fireDate);
    }

    public void setCompareDate(String date) {
        this.compareMonth = date;

        getMonthDiary();
    }

    public void setChangeCompareDate(String date) {
        dates.clear();
        this.compareMonth = date;

        getMonthDiary();
    }
    public void resetDiary(){
        this.title = null;
        this.content = null;
        this.emoticonNum = null;
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

    // Firebase에서 월별 일기 조회
    public void getMonthDiary() {

        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/" + loginUser.getUid() + "/");

        diaryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dates.clear();

                for (DataSnapshot dateSnap : dataSnapshot.getChildren()) { //하위 구조 (작성일자)
                    if (compareMonth.equals(dateSnap.getKey().substring(0, 6))) {
                        Diary value = dateSnap.getValue(Diary.class);
                        monthlyDate = value.date + value.emoticonNum;
                        dates.add(monthlyDate);
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
        resetDiary();

        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/" + loginUser.getUid() + "/" + date);

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
        diaryRef = database.getReference("/일기장/" + loginUser.getUid() + "/");


        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;

        Diary diary = new Diary(value.title, value.content, value.emoticonNum, value.date);   //model Diary 객체
        diaryValues = diary.toMap();
        childUpdates.put(fireDate, diaryValues); //diaryValues가 null이면 기존 데이터 삭제됨
        diaryRef.updateChildren(childUpdates);
    }

    // 일기 삭제
    public void deleteDiary(String date) {

        // 게시글 삭제
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/" + loginUser.getUid() + "/");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(fireDate, null); //dnull이라 기존 데이터 삭제됨
        diaryRef.updateChildren(childUpdates);
        String deleteDate =fireDate.substring(7,9)+date;

        // 이미지 삭제
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference imgSaveRef = storageRef.child(saveId);

        imgSaveRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                System.out.println("삭제성공");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                System.out.println("error - "+ exception.getMessage());
            }
        });
        dates.remove(deleteDate);
    }

    // 이미지 비트맵 저장, 전달
    public void setImgName() {
        imgName = DiaryViewModel.getImgName();

        if (imgName != null) {
            saveImg(imgName);
        }
    }

    //  이미지 저장
    public void saveImg(Bitmap imgBitmap) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        StorageReference imgSaveRef = storageRef.child(saveId);

        Bitmap bitmap = imgBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgSaveRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("error - " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                System.out.println("성공");
            }
        });
    }
}




