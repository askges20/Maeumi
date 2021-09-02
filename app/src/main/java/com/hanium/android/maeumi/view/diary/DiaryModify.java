package com.hanium.android.maeumi.view.diary;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.DiaryModel;

import java.io.InputStream;

public class DiaryModify extends AppCompatActivity {

    DiaryModel DiaryModel = new DiaryModel();

    String diaryCalDate, diaryTitle, diaryContent, diaryEmoticonNum;
    TextView dateText, titleText, contentText;
    ConstraintLayout mainContent;
    ImageView imgView,emoticon;
    Bitmap imgName;
    Button addImgBtn,deleteImgBtn;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_modify);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        // 키보드 화면 밀림 제거
        imgName = null;
        imgUri = null;
        DiaryModel.setImgNameBitmap(null);
        DiaryModel.setImgNameUri(null);

        diaryCalDate = DiaryModel.getCalendarDate();

        dateText = findViewById(R.id.modifyDate);
        titleText = findViewById(R.id.diaryTitleModifyText);
        contentText = findViewById(R.id.diaryContentModifyText);
        mainContent = findViewById(R.id.mainContent);
        imgView = findViewById(R.id.testImgView);
        emoticon = findViewById(R.id.emoticon);

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
                    emoticon.setImageResource(R.drawable.diary_glad);
                    break;
                case "2":
                    emoticon.setImageResource(R.drawable.diary_happy);
                    break;
                case "3":
                    emoticon.setImageResource(R.drawable.diary_calm);
                    break;
                case "4":
                    emoticon.setImageResource(R.drawable.diary_angry);
                    break;
                case "5":
                    emoticon.setImageResource(R.drawable.diary_sad);
                    break;
                case "6":
                    emoticon.setImageResource(R.drawable.diary_worried);
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
                Glide.with(DiaryModify.this).load(uri).into(imgView);
                addImgBtn.setText("사진 바꾸기");
                deleteImgBtn.setVisibility(View.VISIBLE);
                imgUri = uri;
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

        if (diaryTitle.equals("")){
            Toast.makeText(DiaryModify.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (diaryContent.equals("")) {
            Toast.makeText(DiaryModify.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            if (imgName != null) {
                DiaryModel.setImgNameBitmap(imgName);
                Intent pickIntent = new Intent(DiaryModify.this, DiaryEmoticonPick.class);
                pickIntent.putExtra("title", diaryTitle);
                pickIntent.putExtra("content", diaryContent);
                startActivity(pickIntent);
            }else{
                if(imgUri != null){
                    DiaryModel.setImgNameUri(imgUri);
                    Intent pickIntent = new Intent(DiaryModify.this, DiaryEmoticonPick.class);
                    pickIntent.putExtra("title", diaryTitle);
                    pickIntent.putExtra("content", diaryContent);
                    startActivity(pickIntent);
                }else{
                    Intent pickIntent = new Intent(DiaryModify.this, DiaryEmoticonPick.class);
                    pickIntent.putExtra("title", diaryTitle);
                    pickIntent.putExtra("content", diaryContent);
                    startActivity(pickIntent);
                }
            }

            finish();   //현재 액티비티 없애기
        }
    }

    public void setEmptyImg(View view){
        Glide.with(getApplicationContext()).clear(imgView);
        imgName = null;
        imgUri = null;
        DiaryModel.setImgNameUri(null);
        DiaryModel.setImgNameBitmap(null);
        addImgBtn.setText("사진 추가");
        deleteImgBtn.setVisibility(View.GONE);
    }


    //목록으로 버튼 클릭 시
    public void goToBack(View view) {
        finish();
    }

}