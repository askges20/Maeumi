package com.hanium.android.maeumi.model;

import android.graphics.Bitmap;
import android.net.Uri;

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
import com.hanium.android.maeumi.adapters.CalendarAdapter;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.view.diary.DiaryMain;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiaryModel {

    DiaryMain DiaryMain;
    CalendarAdapter adapter;

    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference diaryRef;
    LoginUser loginUser = LoginUser.getInstance();

    public static String calendarDate, fireDate, compareMonth, saveId;
    public static String title, content, nullDiary, emoticonNum,diaryWriteDate, varTitle, varContent, varENum,varWDate;
    public static String day, year, month, oneTimeDate, oneTimeMonth;
    public static Bitmap imgNameBitmap;
    public static Uri imgNameUri;
    public static boolean dateCheckResult;

    public static ArrayList<String> dates = new ArrayList<>();

    public DiaryModel() {
    }

    public DiaryModel(DiaryMain custom, CalendarAdapter adapter) {
        this.DiaryMain = custom;
        this.adapter = adapter;
    }

    public void setCompareMonth(LocalDate selectedDate) {
        this.oneTimeMonth = selectedDate.toString();
        this.compareMonth = this.oneTimeMonth.replace("-", "");
        this.compareMonth = this.compareMonth.substring(0, 6);

        getMonthDiaryFB();
    }

    public void getMonthDiaryFB() {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/" + loginUser.getUid() + "/");

        diaryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dates.clear();

                for (DataSnapshot dateSnap : dataSnapshot.getChildren()) {
                    if (compareMonth.equals(dateSnap.getKey().substring(0, 6))) {
                        Diary value = dateSnap.getValue(Diary.class);
                        String monthlyDate = value.date + value.emoticonNum;   //일기 있는 날짜와 이모티콘 번호
                        dates.add(monthlyDate);
                    }
                }
                giveDiaryDatesToMain();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    public void giveDiaryDatesToMain() {
        DiaryMain.setDates(this.dates);
    }

    // Intent에서 생성된 날짜를 FireBase, 캘린더 날짜로 바꾸고 저장
    public void setDate(LocalDate monthYear, String date) {
        this.oneTimeDate = monthYear.toString();
        this.year = this.oneTimeDate.substring(0, 4);
        this.month = this.oneTimeDate.substring(5, 7);
        this.day = dayPlusZero(date);
        compareDay(date);
        this.fireDate = "/" + this.year + this.month + this.day + "/";
        if (date == "") {
            this.calendarDate = null;
        } else {
            this.calendarDate = this.year + "년 " + this.month + "월 " + this.day + "일";
        }

        saveId = LoginUser.getInstance().getUid() + fireDate;
        getDiaryFromFB(fireDate);
    }
    public void setChangedDate(String date,String calDate){
        this.fireDate = date;
        this.calendarDate = calDate;
        this.compareMonth = date.substring(1,7);
        this.day = date.substring(7,9);
        saveId = LoginUser.getInstance().getUid() + date;
        getDiaryFromFB(fireDate);
    }

    public void compareDay(String date){
        //일기에서 선택한 날짜
        String strPickDate = oneTimeDate.substring(0,8)+date;

        // 오늘
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        String strToday = sdFormat.format(nowDate);

        try{
            Date pickDate = sdFormat.parse(strPickDate);
            Date today = sdFormat.parse(strToday);
            if(today.after(pickDate)){
               this.dateCheckResult = true;
            }else if(today.equals(pickDate)){
                this.dateCheckResult = true;
            }else{
                this.dateCheckResult = false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public String dayPlusZero(String date) {
        if (date.length() < 2) {
            date = "0" + date;
        }
        return date;
    }

    // 일기 제목, 내용, 이모티콘 번호 불러오기
    public void setDiaryData() {
        resetDiary();
        this.nullDiary = getNullDiary();
        if (this.nullDiary != null) {
            title = getTitleTwo();
            content = getContentTwo();
            emoticonNum = getEmoticonNumTwo();
            diaryWriteDate = getDiaryWriteDateTwo();
        }
    }

    public void resetDiary() {
        this.title = null;
        this.content = null;
        this.emoticonNum = null;
    }


    // Null Diary
    public void setNullDiary(String data) {
        this.nullDiary = data;
    }
    public boolean getDateCheckResult(){
        return dateCheckResult;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleTwo() {
        return varTitle;
    }

    public String getDiaryWriteDate() {
        return diaryWriteDate;
    }

    public String getDiaryWriteDateTwo() {
        return varWDate;
    }

    public String getContentTwo() {
        return varContent;
    }

    public String getEmoticonNumTwo() {
        return varENum;
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

    // 캘린더 날짜 불러오기
    public static String getCalendarDate() {
        return calendarDate;
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

                    varTitle = value.title;
                    varContent = value.content;
                    varENum = value.emoticonNum;
                    varWDate = value.writeDate;
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
    public void diaryWrite(String title, String content,String diaryEmoticon,String wDate) {
        Diary value = new Diary(title, content, diaryEmoticon, this.day,wDate);
        diaryHelloWrite(value);
    }

    // 일기 작성 & 수정
    public void diaryHelloWrite(Diary value) {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/" + loginUser.getUid() + "/");


        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> diaryValues = null;

        Diary diary = new Diary(value.title, value.content, value.emoticonNum, value.date,value.writeDate);   //model Diary 객체
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
        String deleteDate = fireDate.substring(7, 9) + date;

        deleteImg();

        dates.remove(deleteDate);
    }

    // 아이디 + 날짜로 이미지 조회 id 생성
    public String getFireImgName() {
        this.saveId = LoginUser.getInstance().getUid() + fireDate;
        return saveId;
    }

    // 이미지 비트맵 저장, 전달
    public void setImgNameBitmap(Bitmap bitmap) {
        imgNameBitmap = bitmap;
    }
    public Bitmap getImgNameBitmap(){
        return this.imgNameBitmap;
    }
    public void setImgNameUri(Uri uri){
        imgNameUri = uri;
    }
    public Uri getImgNameUri(){
        return this.imgNameUri;
    }

    // 이미지 삭제
    public void deleteImg(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference imgSaveRef = storageRef.child(saveId);

        imgSaveRef.delete();
    }

    public void saveImg(Bitmap imgBitmap) {
        if (imgNameBitmap != null) {

            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference("/diary");

            StorageReference imgSaveRef = storageRef.child(saveId);

            Bitmap bitmap = imgBitmap;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imgSaveRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("error - " + exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("성공");
                }
            });
        }


    }
}
