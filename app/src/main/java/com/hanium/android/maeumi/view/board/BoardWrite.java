package com.hanium.android.maeumi.view.board;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;
import com.hanium.android.maeumi.view.loading.LoginUser;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BoardWrite extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference boardRef;
    Bitmap imgName;
    private String boardType;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        Intent intent = getIntent();
        boardType = intent.getStringExtra("타입");    //작성할 게시판 타입

        //작성 일자 = 현재 날짜
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = dateFormat.format(date);

        Button saveWrite = (Button) findViewById(R.id.saveWrite); //작성 완료 버튼
        Button addPhoto = (Button) findViewById(R.id.addPhoto); //사진 추가 버튼
        imgView = findViewById(R.id.imgView); //사진 추가 버튼
        final EditText boardTitle = (EditText) findViewById(R.id.boardTitle); //게시글 제목
        final EditText boardBody = (EditText) findViewById(R.id.boardBody); //게시글 내용

        //작성완료 버튼 이벤트 리스너
        saveWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = boardTitle.getText().toString(); //작성한 제목
                String content = boardBody.getText().toString();    //작성한 내용
                /*제목, 내용 유효성 검사 추가할 예정*/
                addPost(title, content);    //게시글 등록
                Toast.makeText(BoardWrite.this, "작성 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //사진추가 버튼 이벤트 리스너
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelfPermission();
            }
        });
    }

    protected void getImgIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,101);
    }

    //권한확인
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
            getImgIntent();
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
                    Glide.with(getApplicationContext()).load(imgName).into(imgView);
                    inStream.close();   // 스트림 닫아주기
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 사진 DB저장
    protected void saveImg(){

    }

    protected void addPost(String title, String content){
        database = FirebaseDatabase.getInstance();

        //로그인한 사용자
        LoginUser loginUser = LoginUser.getInstance();
        String loginUserUid = loginUser.getUid();
        String loginUserAlias = loginUser.getAlias();

        String today = getToday();  //yyyyMMdd
        String code = getCode();    //HHmmss
        String postNum = code + loginUserUid; //작성자 Uid + 시간 조합해서 게시글 번호 생성
        String curDate = getCurrentDate();

        //게시판 종류
        switch (boardType){
            case "free":
                boardRef = database.getReference("/게시판/자유게시판/"+today);
                break;
            case "question":
                boardRef = database.getReference("/게시판/질문게시판/"+today);
                break;
            case "tip":
                boardRef = database.getReference("/게시판/꿀팁게시판/"+today);
                break;
            case "anonymous":
                boardRef = database.getReference("/게시판/익명게시판/"+today);
                break;
            default:
                break;
        }

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        Post post = new Post(title, content, loginUserAlias, curDate, loginUserUid);   //model Post 객체
        postValues = post.toMap();
        childUpdates.put(postNum, postValues);
        boardRef.updateChildren(childUpdates);
        post.saveImg(imgName);
    }

    protected String getToday(){    //오늘 날짜 yyyyMMdd
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
        return format.format(time);
    }

    protected String getCode(){    //게시글 번호 용도
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat ( "HHmmss");
        return format.format(time);
    }

    protected String getCurrentDate(){  //날짜, 시각
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    public void goToBack(View view) {
        finish();
    }

}
