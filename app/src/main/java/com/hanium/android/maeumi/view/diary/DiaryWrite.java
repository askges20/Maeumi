package com.hanium.android.maeumi.view.diary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

import java.io.InputStream;

public class DiaryWrite extends AppCompatActivity {

    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    TextView dateText,emoticon;
    EditText titleText,contentText;
    String diaryCalDate, diaryEmoticon;
    LinearLayout mainContent;
    ImageView imgView;
    Bitmap imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryCalDate = DiaryViewModel.getCalendarDate();
        nullDate();

        dateText = findViewById(R.id.writeDate);
        emoticon = findViewById(R.id.emoticon);
        mainContent = findViewById(R.id.mainContent);
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

    public void processAdd(View view){

        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        if(diaryEmoticon == null || diaryEmoticon == ""){
            Toast toastView = Toast.makeText(DiaryWrite.this, "기분을 골라주세요.", Toast.LENGTH_SHORT);
            toastView.show();
        }else{
            DiaryViewModel.diaryWrite(diaryTitle,diaryContent,diaryEmoticon);
            DiaryViewModel.setImgName(imgName);
            Toast toastView = Toast.makeText(DiaryWrite.this, "작성 완료", Toast.LENGTH_SHORT);
            toastView.show();
            finish();   //현재 액티비티 없애기
        }
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
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
                    mainContent.setBackgroundColor(Color.YELLOW);
                } else if (menuItem.getItemId() == R.id.action_menu2) {
                    emoticon.setText("평범");
                    diaryEmoticon = "2";
                    mainContent.setBackgroundColor(Color.GREEN);
                } else {
                    emoticon.setText("나쁨");
                    diaryEmoticon = "3";
                    mainContent.setBackgroundColor(Color.GRAY);
                }

                return false;
            }
        });
        popupMenu.show();
    }

    public void addImg(View view){   // 사진추가 버튼 클릭 시
        checkSelfPermission();

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,101);
    }

    public void checkSelfPermission() {
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
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
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
                    imgView.setImageBitmap(imgName);    // 선택한 이미지 이미지뷰에 셋
                    inStream.close();   // 스트림 닫아주기
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
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

}
