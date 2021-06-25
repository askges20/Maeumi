package com.hanium.android.maeumi.view.board;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardWrite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        //작성 일자 = 현재 날짜
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = dateFormat.format(date);

        TextView boardWriteDate = (TextView)findViewById(R.id.boardWriteDate);
        boardWriteDate.setText(formatDate);

        Button saveWrite = (Button)findViewById(R.id.saveWrite); //작성 완료 버튼
        final EditText boardTitle = (EditText)findViewById(R.id.boardTitle); //게시글 제목
        final EditText boardBody = (EditText)findViewById(R.id.boardBody); //게시글 내용

        //작성완료 버튼 이벤트 리스너
        saveWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BoardWrite.this, "작성완료", Toast.LENGTH_SHORT).show();

                System.out.println("제목: "+ boardTitle.getText());
                System.out.println("내용: "+ boardBody.getText());
                finish();
            }
        });
    }


}
