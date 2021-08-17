package com.hanium.android.maeumi.view.diary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryWrite extends AppCompatActivity {

    DiaryModel DiaryModel = new DiaryModel();

    TextView dateText,emoticon;
    EditText titleText,contentText;
    String diaryCalDate, diaryEmoticon;
    ConstraintLayout mainContent;
    ImageView imgView;
    Bitmap imgName;
    Button addImgBtn,deleteImgBtn;
    DatePicker diaryDatePicker;
    boolean dateCheckResult;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryCalDate = DiaryModel.getCalendarDate();
        nullDate();
        dateCheck();

        dateText = findViewById(R.id.writeDate);
        emoticon = findViewById(R.id.emoticon);
        mainContent = findViewById(R.id.mainContent);
        addImgBtn = findViewById(R.id.addImgBtn);
        deleteImgBtn = findViewById(R.id.deleteImgBtn);
        diaryDatePicker = findViewById(R.id.diaryDatePicker);
        titleText = (EditText)findViewById(R.id.diaryTitleWriteText);
        contentText = (EditText)findViewById(R.id.diaryContentWriteText);
        imgView = findViewById(R.id.testImgView);
        dateText.setText(diaryCalDate);
    }
    // 빈곳 클릭 시 이벤트
    public void nullDate(){
        if(diaryCalDate == null){
            Toast.makeText(DiaryWrite.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void dateCheck(){
        dateCheckResult = DiaryModel.getDateCheckResult();
        if(dateCheckResult ==  false){
            Toast.makeText(DiaryWrite.this, "오늘 이전 날짜에 일기를 작성해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void processAdd(View view){

        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        if(diaryEmoticon == null || diaryEmoticon == ""){
            Toast toastView = Toast.makeText(DiaryWrite.this, "기분을 골라주세요.", Toast.LENGTH_SHORT);
            toastView.show();
        }else{
            if(imgName != null){
                DiaryModel.setImgName(imgName);
            }
            DiaryModel.diaryWrite(diaryTitle,diaryContent,diaryEmoticon);
            Toast toastView = Toast.makeText(DiaryWrite.this, "작성 완료", Toast.LENGTH_SHORT);
            toastView.show();
            finish();   //현재 액티비티 없애기
        }
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        finish();
    }
    public void onFilterClick(final View view) {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.diary_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_menu1) {
                    emoticon.setText("좋음");
                    diaryEmoticon = "1";
                    mainContent.setBackgroundResource(R.color.pinkred);
                } else if (menuItem.getItemId() == R.id.action_menu2) {
                    emoticon.setText("평범");
                    diaryEmoticon = "2";
                    mainContent.setBackgroundResource(R.color.lightgreen);
                } else {
                    emoticon.setText("나쁨");
                    diaryEmoticon = "3";
                    mainContent.setBackgroundResource(R.color.diaryGray);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void addImg(View view){   // 사진추가 버튼 클릭 시
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        // 권한 요청
        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else { // 모두 허용 상태
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent,101);
        }
    }
    public void setEmptyImg(View view){
        Glide.with(getApplicationContext()).clear(imgView);
        imgName =null;
        addImgBtn.setText("사진 추가");
        deleteImgBtn.setVisibility(View.GONE);
    }


    // 앨범에서 이미지 클릭하면 ImgView에 이미지 담아주기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream inStream = resolver.openInputStream(fileUri);
                    imgName = BitmapFactory.decodeStream(inStream);
                    Glide.with(getApplicationContext()).load(imgName).into(imgView);
                    addImgBtn.setText("사진 바꾸기");
                    deleteImgBtn.setVisibility(View.VISIBLE);
                    inStream.close();   // 스트림 닫아주기
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 권한 확인
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                // 동의
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("TestImg 권한 허용 : " + permissions[i]);
                }
            }
        }

    }

    // DatePicker
    public void openDatePicker(View view){
        int year = Integer.parseInt(diaryCalDate.substring(0,4));
        int month = Integer.parseInt(diaryCalDate.substring(6,8))-1;
        int day = Integer.parseInt(diaryCalDate.substring(10,12));
//        getPickerDate(year,month,day);


        diaryDatePicker.setVisibility(View.VISIBLE);
        diaryDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
    }

    public void getPickerDate(int year,int month,int day){
        String sYear = Integer.toString(year);
        String sMonth = Integer.toString(month);
        String sDay = Integer.toString(day);
        String bDate = sYear +"-" +sMonth+"-" + sDay;

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdFormat.parse(bDate);
            long time = date.getTime();
            diaryDatePicker.setMaxDate(time);
        }catch (Exception e){

        }

    }

}
