package com.hanium.android.maeumi.view.board;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.profile.ProfileEdit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardWrite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = dateFormat.format(date);

        TextView boardWriteDate = (TextView)findViewById(R.id.boardWriteDate);
        boardWriteDate.setText(formatDate);

        Button saveWrite = (Button)findViewById(R.id.saveWrite);
        final EditText boardTitle = (EditText)findViewById(R.id.boardTitle);
        final EditText boardBody = (EditText)findViewById(R.id.boardBody);

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
