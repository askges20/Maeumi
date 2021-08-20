package com.hanium.android.maeumi.view.board;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PostModify extends AppCompatActivity {
    PostContent prevActivity;    //수정하는 게시글 내용 페이지

    FirebaseDatabase database;
    DatabaseReference postRef;

    String postTitle, postContent, postDate, postWriter, boardType,postCode;
    String diaryDate, imgPath;

    EditText titleText,contentText;
    ImageView boardImgView;
    Bitmap imgName;

    TextView addImgBtn, deleteImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_modify);
        prevActivity = (PostContent)PostContent.PostContent_Activity;   //이전 액티비티

        titleText = findViewById(R.id.postTitleText);
        contentText = findViewById(R.id.postContentText);
        boardImgView = findViewById(R.id.imgView);

        addImgBtn = findViewById(R.id.addImgBtn);
        deleteImgBtn = findViewById(R.id.deleteImgBtn);

        Intent prevIntent = getIntent();
        postTitle = prevIntent.getStringExtra("title");
        postContent = prevIntent.getStringExtra("content");
        postDate = prevIntent.getStringExtra("writeDate");
        postWriter = prevIntent.getStringExtra("writer");
        boardType = prevIntent.getStringExtra("boardType");
        postCode = prevIntent.getStringExtra("postCode");

        diaryDate = postDate.substring(0, 4) + postDate.substring(5, 7) + postDate.substring(8, 10);
        imgPath = diaryDate + postCode;

        titleText.setText(postTitle);
        contentText.setText(postContent);

        getImg();
    }

    public void processModify(View view){   //수정 완료 버튼 클릭 시
        //작성한 일기 제목, 내용
        String newTitle = titleText.getText().toString();
        String newContent = contentText.getText().toString();

        //DB 반영
        database = FirebaseDatabase.getInstance();
        String date = postDate.substring(0,4)+postDate.substring(5,7)+postDate.substring(8,10);

        switch (boardType){
            case "free":
                postRef = database.getReference("/게시판/자유게시판/"+date+"/");
                break;
            case "question":
                postRef = database.getReference("/게시판/질문게시판/"+date+"/");
                break;
            case "tip":
                postRef = database.getReference("/게시판/꿀팁게시판/"+date+"/");
                break;
            case "anonymous":
                postRef = database.getReference("/게시판/익명게시판/"+date+"/");
                break;
            default:
                break;
        }

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        String time = postDate.substring(11,13)+postDate.substring(14,16)+postDate.substring(17,19);
        Post post = new Post(newTitle, newContent, postWriter, postDate, LoginUser.getInstance().getUid());   //model Diary 객체
        postValues = post.toMap();
        childUpdates.put(time + LoginUser.getInstance().getUid(), postValues);
        postRef.updateChildren(childUpdates);

        Toast toastView = Toast.makeText(PostModify.this, "수정 완료", Toast.LENGTH_SHORT);
        toastView.show();
        prevActivity.finish();  //게시글 내용 페이지도 동시에 닫기

        if(imgName != null){
            post.saveImg(imgName,boardType,imgPath);
        }else{
            deleteImg();
        }
        finish();
    }

    // 이미지 가져오기
    private void getImg() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("/board");


        if(imgPath != null){
            storageRef.child(boardType).child(imgPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String tet = uri.toString();
                    Glide.with(PostModify.this).load(tet).into(boardImgView);
                    addImgBtn.setText("사진 바꾸기");
                    deleteImgBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void setEmptyImg(View view){
        Glide.with(getApplicationContext()).clear(boardImgView);
        imgName = null;
        addImgBtn.setText("사진 추가");
        deleteImgBtn.setVisibility(View.GONE);
    }

    private void deleteImg(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("/board");

        storageRef.child(boardType).child(imgPath).delete();
    }

    public void changeImg(View view){
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
                    Glide.with(getApplicationContext()).load(imgName).into(boardImgView);
                    addImgBtn.setText("사진 바꾸기");
                    deleteImgBtn.setVisibility(View.VISIBLE);
                    inStream.close();   // 스트림 닫아주기
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void goToBack(View view){
        finish();   //현재 액티비티 없애기
    }
}
