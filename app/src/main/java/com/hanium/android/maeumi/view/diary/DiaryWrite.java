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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;
import com.hanium.android.maeumi.view.loading.LoginUser;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryWrite extends AppCompatActivity {

    DiaryModel DiaryModel = new DiaryModel();

    TextView dateText;
    EditText titleText, contentText;
    String diaryCalDate;
    ConstraintLayout mainContent;
    LinearLayout diaryDatePickerContainer;
    ImageView imgView;
    Bitmap imgName;
    Button addImgBtn, deleteImgBtn, completeDateChangeBtn,diaryDateChangeOpenBtn;
    DatePicker diaryDatePicker;
    int cYear, cMonth, cDay = 0;
    boolean dateCheckResult;

    FirebaseDatabase database;
    DatabaseReference diaryRef;

    boolean isWrittenDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        imgName = null;
        DiaryModel.setImgNameBitmap(null);

        SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy??? MM??? dd???");
        diaryCalDate = dateFormat.format(new Date());
//        nullDate();
//        dateCheck();

        dateText = findViewById(R.id.writeDate);
        mainContent = findViewById(R.id.mainContent);
        addImgBtn = findViewById(R.id.addImgBtn);
        deleteImgBtn = findViewById(R.id.deleteImgBtn);
        completeDateChangeBtn = findViewById(R.id.completeDateChangeBtn);
        diaryDateChangeOpenBtn = findViewById(R.id.diaryDateChangeOpenBtn);
        diaryDatePicker = findViewById(R.id.diaryDatePicker);
        diaryDatePickerContainer = findViewById(R.id.diaryDatePickerContainer);
        titleText = (EditText) findViewById(R.id.diaryTitleWriteText);
        contentText = (EditText) findViewById(R.id.diaryContentWriteText);
        imgView = findViewById(R.id.testImgView);
        dateText.setText(diaryCalDate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        //????????? ?????? ?????? ??????


        getChangedDate(null);   //?????? ????????? ?????????
    }

//    // ?????? ?????? ??? ?????????
//    public void nullDate() {
//        if (diaryCalDate == null) {
//            Toast.makeText(DiaryWrite.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
//    public void dateCheck() {
//        dateCheckResult = DiaryModel.getDateCheckResult();
//        if (dateCheckResult == false) {
//            Toast.makeText(DiaryWrite.this, "?????? ?????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }

    public void processAdd(View view) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd a hh:mm");
        Date date = new Date();
        String strToday = sdFormat.format(date);

        //????????? ?????? ??????, ??????
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        if (diaryDatePickerContainer.getVisibility() == View.VISIBLE) {
            Toast.makeText(DiaryWrite.this, "?????? ?????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }else if (diaryTitle.equals("")){
            Toast.makeText(DiaryWrite.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        } else if (diaryContent.equals("")) {
            Toast.makeText(DiaryWrite.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        } else if (isWrittenDate) {
            Toast.makeText(DiaryWrite.this, "?????? ????????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
        }else {
            if (imgName != null) {
                DiaryModel.setImgNameBitmap(imgName);
            }
            Intent pickIntent = new Intent(DiaryWrite.this, DiaryEmoticonPick.class);
            pickIntent.putExtra("title", diaryTitle);
            pickIntent.putExtra("content", diaryContent);
            pickIntent.putExtra("wDate", strToday);
            pickIntent.putExtra("from", "write");
            startActivity(pickIntent);

            finish();   //?????? ???????????? ?????????
        }
    }

    public void goToBack(View view) {   //???????????? ?????? ?????? ???
        finish();
    }

    public void addImg(View view) {   // ???????????? ?????? ?????? ???
        String temp = "";
        //?????? ?????? ?????? ??????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //?????? ?????? ?????? ??????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        // ?????? ??????
        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else { // ?????? ?????? ??????
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, 101);
        }
    }

    public void setEmptyImg(View view) {
        Glide.with(getApplicationContext()).clear(imgView);
        imgName = null;
        DiaryModel.setImgNameBitmap(null);
        DiaryModel.setImgNameUri(null);
        addImgBtn.setText("?????? ??????");
        deleteImgBtn.setVisibility(View.GONE);
    }


    // ???????????? ????????? ???????????? ImgView??? ????????? ????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // ?????????
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream inStream = resolver.openInputStream(fileUri);
                    imgName = BitmapFactory.decodeStream(inStream);
                    Glide.with(getApplicationContext()).load(imgName).into(imgView);
                    addImgBtn.setText("?????? ?????????");
                    deleteImgBtn.setVisibility(View.VISIBLE);
                    inStream.close();   // ????????? ????????????
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "?????? ???????????? ??????", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //????????? ?????? ?????? ??????
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                // ??????
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("TestImg ?????? ?????? : " + permissions[i]);
                }
            }
        }

    }

    // DatePicker
    public void openDatePicker(View view) {
        diaryDateChangeOpenBtn.setVisibility(View.GONE);
        diaryDatePickerContainer.setVisibility(View.VISIBLE);

        if(cYear == 0 && cDay == 0){
            int year = Integer.parseInt(diaryCalDate.substring(0, 4));
            int month = Integer.parseInt(diaryCalDate.substring(6, 8)) - 1;
            int day = Integer.parseInt(diaryCalDate.substring(10, 12));

            diaryDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cYear = year;
                    cMonth = monthOfYear +1;
                    cDay = dayOfMonth;
                }
            });
        }else{
            diaryDatePicker.init(cYear, cMonth-1, cDay, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cYear = year;
                    cMonth = monthOfYear +1;
                    cDay = dayOfMonth;
                }
            });
        }
    }

    public void getPickerDate(String date, String viewDate, String fireDate) {
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/?????????/" + LoginUser.getInstance().getUid() + "/" + fireDate);


        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        String strToday = sdFormat.format(nowDate);

        try {
            Date pickDate = sdFormat.parse(date);
            Date today = sdFormat.parse(strToday);
            diaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // ????????? ?????? ????????????
                    if (snapshot.exists()) {
                        Toast.makeText(DiaryWrite.this, "?????? ????????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        isWrittenDate = true;    //?????? ????????? ?????? ????????? ??????
                    } else {
                        if (today.after(pickDate) || today.equals(pickDate)) {
                            dateText.setText(viewDate);
                            diaryDatePickerContainer.setVisibility(View.GONE);
                            DiaryModel.setChangedDate(fireDate, viewDate);
                            diaryDateChangeOpenBtn.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(DiaryWrite.this, "?????? ?????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeDatePicker(View view) {
        diaryDateChangeOpenBtn.setVisibility(View.VISIBLE);
        diaryDatePickerContainer.setVisibility(View.GONE);
    }

    public void getChangedDate(View view) {

        String year = Integer.toString(cYear);
        String month = Integer.toString(cMonth);
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = Integer.toString(cDay);
        if (day.length() == 1) {
            day = "0" + day;
        }
        if(year.equals("0") || day.equals("00")){
            year = diaryCalDate.substring(0, 4);
            month = diaryCalDate.substring(6, 8);
            day = diaryCalDate.substring(10, 12);
        }

        String date = year + "-" + month + "-" + day;
        String viewDate = year + "??? " + month + "??? " + day + "???";
        String fireDate = "/" + year + month + day + "/";
        getPickerDate(date, viewDate, fireDate);

    }

}
