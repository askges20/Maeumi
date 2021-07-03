package com.hanium.android.maeumi.view.diary;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.viewmodel.DiaryViewModel;

public class DiaryWrite extends AppCompatActivity {

    DiaryViewModel DiaryViewModel = new DiaryViewModel();

    TextView date,emoticon;
    EditText diaryTitleText,diaryContentText;

    String diaryDate, diaryEmoticon;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryDate = DiaryViewModel.getCalendarDate();
        nullDate();

        date = findViewById(R.id.writeDate);
        emoticon = findViewById(R.id.emoticon);
        diaryTitleText = (EditText)findViewById(R.id.diaryTitleWriteText);
        diaryContentText = (EditText)findViewById(R.id.diaryContentWriteText);
        date.setText(diaryDate);
    }
    // 빈곳 클릭 시 이벤트
    public void nullDate(){
        if(diaryDate == null){
            Toast.makeText(DiaryWrite.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //작성 완료 버튼 클릭 시 DB 반영   -> 추후 view가 아닌 다른 패키지로 옮길 예정
    public void processAdd(View view){

        //작성한 일기 제목, 내용
        String diaryTitle = diaryTitleText.getText().toString();
        String diaryContent = diaryContentText.getText().toString();

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
                } else if (menuItem.getItemId() == R.id.action_menu2) {
                    emoticon.setText("평범");
                    diaryEmoticon = "2";
                } else {
                    emoticon.setText("나쁨");
                    diaryEmoticon = "3";
                }

                return false;
            }
        });
        popupMenu.show();
    }
}
