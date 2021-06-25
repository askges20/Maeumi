package com.hanium.android.maeumi.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BoardWrite extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference boardRef;

    private int boardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        Intent intent = getIntent();
        boardType = intent.getIntExtra("타입", 1);    //작성할 게시판 타입

        //작성 일자 = 현재 날짜
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = dateFormat.format(date);

        TextView boardWriteDate = (TextView) findViewById(R.id.boardWriteDate);
        boardWriteDate.setText(formatDate);

        Button saveWrite = (Button) findViewById(R.id.saveWrite); //작성 완료 버튼
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
                System.out.println("제목: " + boardTitle.getText());
                System.out.println("내용: " + boardBody.getText());

                finish();
            }
        });
    }

    protected void addPost(String title, String content){
        database = FirebaseDatabase.getInstance();

        String today = getToday();
        String postNum = "아이디" + today; //나중에 게시글 번호?로 수정할 듯
        String curDate = getCurrentDate();

        //게시판 종류
        if (boardType==1)
            boardRef = database.getReference("/자유게시판/"+today);
        else
            boardRef = database.getReference("/익명게시판/"+today);

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        Post post = new Post(title, content, "아이디", curDate);   //model Post 객체
        postValues = post.toMap();
        System.out.println("postValue- " + postValues);
        childUpdates.put(postNum, postValues);
        boardRef.updateChildren(childUpdates);
    }

    protected String getToday(){    //날짜
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
        return format.format(time);
    }

    protected String getCurrentDate(){  //날짜, 시각
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

}
