package com.hanium.android.maeumi.view.diary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;

import java.io.InputStream;

public class DiaryModify extends Activity {

    DiaryModel DiaryModel = new DiaryModel();

    String diaryCalDate, diaryTitle, diaryContent, diaryEmoticonNum;
    TextView dateText, titleText, contentText, emoticon;
    ConstraintLayout mainContent;
    ImageView imgView;
    static Bitmap imgName;
    Button addImgBtn,deleteImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_modify);

        diaryCalDate = DiaryModel.getCalendarDate();

        dateText = findViewById(R.id.modifyDate);
        emoticon = findViewById(R.id.emoticon);
        titleText = findViewById(R.id.diaryTitleModifyText);
        contentText = findViewById(R.id.diaryContentModifyText);
        mainContent = findViewById(R.id.mainContent);
        imgView = findViewById(R.id.testImgView);

        addImgBtn = findViewById(R.id.addImgBtn);
        deleteImgBtn = findViewById(R.id.deleteImgBtn);

        diaryTitle = DiaryModel.getTitle();
        diaryContent = DiaryModel.getContent();
        diaryEmoticonNum = DiaryModel.getEmoticonNum();

        dateText.setText(diaryCalDate);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);
        switch (diaryEmoticonNum) {
            case "1":
                emoticon.setText("좋음");
                mainContent.setBackgroundResource(R.color.pinkred);
                break;
            case "2":
                emoticon.setText("평범");
                mainContent.setBackgroundResource(R.color.lightgreen);
                break;
            case "3":
                emoticon.setText("나쁨");
                mainContent.setBackgroundResource(R.color.diaryGray);
                break;
        }
        getImg();
    }

    // 이미지 조회
    private void getImg() {
        String imgString = DiaryModel.getFireImgName();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("/diary");

        storageRef.child(imgString).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String tet = uri.toString();
                Glide.with(DiaryModify.this).load(tet).into(imgView);
                addImgBtn.setText("사진 바꾸기");
                deleteImgBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    // 사진 바꾸기 버튼 클릭
    public void changeImg(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 101);
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

    //수정 완료 버튼 클릭 시
    public void processModify(View view) {
        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        if (diaryEmoticonNum == null || diaryEmoticonNum == "") {
            Toast toastView = Toast.makeText(DiaryModify.this, "기분을 골라주세요.", Toast.LENGTH_SHORT);
            toastView.show();
        } else {
            if(imgName != null){
                DiaryModel.setImgName(imgName);
            }else{
                deleteImg();
            }
            DiaryModel.diaryWrite(diaryTitle, diaryContent, diaryEmoticonNum);
            Toast toastView = Toast.makeText(DiaryModify.this, "수정 완료", Toast.LENGTH_SHORT);
            toastView.show();

            Intent intent = new Intent(DiaryModify.this, DiaryMain.class);
            startActivity(intent);
            finish();   //현재 페이지 닫기
        }
    }

    public void setEmptyImg(View view){
        Glide.with(getApplicationContext()).clear(imgView);
        imgName = null;
        addImgBtn.setText("사진 추가");
        deleteImgBtn.setVisibility(View.GONE);
    }

    private void deleteImg(){
        String imgString = DiaryModel.getFireImgName();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("/diary");

        storageRef.child(imgString).delete();
    }

    //목록으로 버튼 클릭 시
    public void goToBack(View view) {
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
                    diaryEmoticonNum = "1";
                    mainContent.setBackgroundResource(R.color.pinkred);
                } else if (menuItem.getItemId() == R.id.action_menu2) {
                    emoticon.setText("평범");
                    diaryEmoticonNum = "2";
                    mainContent.setBackgroundResource(R.color.lightgreen);
                } else {
                    emoticon.setText("나쁨");
                    diaryEmoticonNum = "3";
                    mainContent.setBackgroundResource(R.color.diaryGray);
                }

                return false;
            }
        });
        popupMenu.show();
    }
}