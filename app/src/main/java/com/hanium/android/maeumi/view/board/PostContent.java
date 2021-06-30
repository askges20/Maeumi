package com.hanium.android.maeumi.view.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.R;

import java.util.HashMap;
import java.util.Map;

public class PostContent extends AppCompatActivity {
    public static Activity PostContent_Activity;

    FirebaseDatabase database;
    DatabaseReference postRef;

    String title, content, writeDate, writer, boardType;

    TextView titleText; //제목 텍스트
    TextView contentText;   //내용 텍스트
    TextView dateText;  //날짜 텍스트
    TextView writerText;    //작성자 텍스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        PostContent_Activity = this;

        titleText = findViewById(R.id.postTitleText);
        contentText = findViewById(R.id.postContentText);
        dateText = findViewById(R.id.postDateText);
        writerText = findViewById(R.id.postWriterText);

        Intent prevIntent = getIntent();
        title = prevIntent.getStringExtra("title");
        titleText.setText(title);
        content = prevIntent.getStringExtra("content");
        contentText.setText(content);
        writeDate = prevIntent.getStringExtra("writeDate");
        dateText.setText(writeDate);
        writer = prevIntent.getStringExtra("writer");
        writerText.setText(writer);
        boardType = prevIntent.getStringExtra("boardType");
    }

    public void goToPostModify(View view) { //수정 버튼 클릭 시
        Intent intent = new Intent(PostContent.this, PostModify.class);

        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("writeDate", writeDate);
        intent.putExtra("writer", writer);
        intent.putExtra("boardType", boardType);

        startActivity(intent);
        System.out.println("Move To Modify Post");
    }

    public void showDeleteDialog(View view) {    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(PostContent.this);
        dialog.setMessage("작성하신 글을 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                //DB에서 삭제
                database = FirebaseDatabase.getInstance();
                String date = writeDate.substring(0, 4) + writeDate.substring(5, 7) + writeDate.substring(8, 10);
                if (boardType.equals("free"))
                    postRef = database.getReference("/자유게시판/"+date+"/");
                else
                    postRef = database.getReference("/익명게시판/"+date+"/");
                String time = writeDate.substring(11,13)+writeDate.substring(14,16)+writeDate.substring(17,19);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("아이디"+time, null);
                postRef.updateChildren(childUpdates);


                Toast toastView = Toast.makeText(PostContent.this, "삭제 완료", Toast.LENGTH_SHORT);
                toastView.show();
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(PostContent.this, "삭제 취소", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void goToBack(View view) {   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
}
