package com.hanium.android.maeumi.view.board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

public class PostContent extends AppCompatActivity {
    TextView titleText; //제목 텍스트
    TextView contentText;   //내용 텍스트
    TextView dateText;  //날짜 텍스트
    TextView writerText;    //작성자 텍스트

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        titleText = findViewById(R.id.postTitleText);
        contentText = findViewById(R.id.postContentText);
        dateText = findViewById(R.id.postDateText);
        writerText = findViewById(R.id.postWriterText);

        Intent prevIntent = getIntent();
        titleText.setText(prevIntent.getStringExtra("title"));
        contentText.setText(prevIntent.getStringExtra("content"));
        dateText.setText(prevIntent.getStringExtra("writeDate"));
        writerText.setText(prevIntent.getStringExtra("writer"));
    }

    public void goToPostModify(View view){ //수정 버튼 클릭 시
        //Intent intent = new Intent(PostContent.this, PostModify.class);
        //startActivity(intent);
        System.out.println("Move To Modify Post");
    }

    public void showDeleteDialog(View view){    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(PostContent.this);
        dialog.setMessage("작성하신 글을 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //DiaryViewModel.deletePost();

                Toast toastView = Toast.makeText(PostContent.this, "삭제 완료", Toast.LENGTH_SHORT);
                toastView.show();
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(PostContent.this,"삭제 취소", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
}
