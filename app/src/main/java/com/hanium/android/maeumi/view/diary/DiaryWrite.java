package com.hanium.android.maeumi.view.diary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

public class DiaryWrite extends AppCompatActivity {

    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    TextView dateText,emoticon;
    EditText titleText,contentText;
    String diaryCalDate, diaryEmoticon;
    LinearLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryCalDate = DiaryViewModel.getCalendarDate();
        nullDate();

        dateText = findViewById(R.id.writeDate);
        emoticon = findViewById(R.id.emoticon);
        mainContent = findViewById(R.id.mainContent);
        titleText = (EditText)findViewById(R.id.diaryTitleWriteText);
        contentText = (EditText)findViewById(R.id.diaryContentWriteText);
        dateText.setText(diaryCalDate);
    }
    // 빈곳 클릭 시 이벤트
    public void nullDate(){
        if(diaryCalDate == null){
            Toast.makeText(DiaryWrite.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void processAdd(View view){

        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        if(diaryEmoticon == null || diaryEmoticon == ""){
            Toast toastView = Toast.makeText(DiaryWrite.this, "기분을 골라주세요.", Toast.LENGTH_SHORT);
            toastView.show();
        }else{
            DiaryViewModel.diaryWrite(diaryTitle,diaryContent,diaryEmoticon);
            Toast toastView = Toast.makeText(DiaryWrite.this, "작성 완료", Toast.LENGTH_SHORT);
            toastView.show();
            finish();   //현재 액티비티 없애기
        }
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }
    public void onFilterClick(final View view) {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.diary_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_menu1) {
                    emoticon.setText("좋음");
                    diaryEmoticon = "1";
                    mainContent.setBackgroundColor(Color.YELLOW);
                } else if (menuItem.getItemId() == R.id.action_menu2) {
                    emoticon.setText("평범");
                    diaryEmoticon = "2";
                    mainContent.setBackgroundColor(Color.GREEN);
                } else {
                    emoticon.setText("나쁨");
                    diaryEmoticon = "3";
                    mainContent.setBackgroundColor(Color.GRAY);
                }

                return false;
            }
        });
        popupMenu.show();
    }
}
