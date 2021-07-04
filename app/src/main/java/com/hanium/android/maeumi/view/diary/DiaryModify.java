package com.hanium.android.maeumi.view.diary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

public class DiaryModify extends Activity {

    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    String diaryCalDate,diaryTitle, diaryContent,diaryEmoticonNum,diaryEmoticon;
    TextView dateText ,titleText,contentText,emoticon;
    LinearLayout mainContent;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_modify);

        diaryCalDate = DiaryViewModel.getCalendarDate();

        dateText = findViewById(R.id.modifyDate);
        emoticon = findViewById(R.id.emoticon);
        titleText = findViewById(R.id.diaryTitleModifyText);
        contentText = findViewById(R.id.diaryContentModifyText);
        mainContent = findViewById(R.id.mainContent);

        diaryTitle = DiaryViewModel.getTitle();
        diaryContent = DiaryViewModel.getContent();
        diaryEmoticonNum = DiaryViewModel.getEmoticonNum();

        dateText.setText(diaryCalDate);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);
        switch (diaryEmoticonNum){
            case "1":
                emoticon.setText("좋음");
                mainContent.setBackgroundColor(Color.YELLOW);
                break;
            case "2":
                emoticon.setText("평범");
                mainContent.setBackgroundColor(Color.GREEN);
                break;
            case "3":
                emoticon.setText("나쁨");
                mainContent.setBackgroundColor(Color.GRAY);
                break;
        }
    }

    public void processModify(View view){   //수정 완료 버튼 클릭 시
        //작성한 일기 제목, 내용
        String diaryTitle = titleText.getText().toString();
        String diaryContent = contentText.getText().toString();

        if(diaryEmoticon == null || diaryEmoticon == ""){
            Toast toastView = Toast.makeText(DiaryModify.this, "기분을 골라주세요.", Toast.LENGTH_SHORT);
            toastView.show();
        }else{
            DiaryViewModel.diaryWrite(diaryTitle,diaryContent,diaryEmoticon);
            Toast toastView = Toast.makeText(DiaryModify.this, "작성 완료", Toast.LENGTH_SHORT);
            toastView.show();
            Intent intent = new Intent(DiaryModify.this, DiaryMain.class);
            startActivity(intent);
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