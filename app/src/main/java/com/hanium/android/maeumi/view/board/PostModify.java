package com.hanium.android.maeumi.view.board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;

import java.util.HashMap;
import java.util.Map;

public class PostModify extends AppCompatActivity {
    PostContent prevActivity;    //수정하는 게시글 내용 페이지

    FirebaseDatabase database;
    DatabaseReference postRef;

    String postTitle, postContent, postDate, postWriter, boardType;

    EditText titleText;
    EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_modify);
        prevActivity = (PostContent)PostContent.PostContent_Activity;   //이전 액티비티

        titleText = findViewById(R.id.postTitleText);
        contentText = findViewById(R.id.postContentText);

        Intent prevIntent = getIntent();
        postTitle = prevIntent.getStringExtra("title");
        postContent = prevIntent.getStringExtra("content");
        postDate = prevIntent.getStringExtra("writeDate");
        postWriter = prevIntent.getStringExtra("writer");
        boardType = prevIntent.getStringExtra("boardType");

        titleText.setText(postTitle);
        contentText.setText(postContent);
    }

    public void processModify(View view){   //수정 완료 버튼 클릭 시
        //작성한 일기 제목, 내용
        String newTitle = titleText.getText().toString();
        String newContent = contentText.getText().toString();

        //DB 반영
        database = FirebaseDatabase.getInstance();
        String date = postDate.substring(0,4)+postDate.substring(5,7)+postDate.substring(8,10);
        if (boardType.equals("free"))
            postRef = database.getReference("/자유게시판/"+date+"/");
        else
            postRef = database.getReference("/익명게시판/"+date+"/");

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
        finish();
    }

    public void goToBack(View view){
        finish();   //현재 액티비티 없애기
    }
}
