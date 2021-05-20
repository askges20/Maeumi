package com.hanium.android.maeumi.view.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Diary;

public class DiaryContent extends Activity {
    TextView date;
    int year;
    int month;
    int dayOfMonth;

    FirebaseDatabase database;
    DatabaseReference diaryRef;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);

        Intent dateIntent = getIntent();
        year = dateIntent.getIntExtra("연", 1);
        month = dateIntent.getIntExtra("월", 1);
        dayOfMonth = dateIntent.getIntExtra("일", 1);;

        date = findViewById(R.id.contentDate);
        date.setText(year + "/" + month + "/" + dayOfMonth);

        getData(year,month,dayOfMonth);
    }

    public void goToDiaryModify(View view){ //수정 버튼 클릭 시
        Intent intent = new Intent(DiaryContent.this, DiaryModify.class);
        intent.putExtra("연", year);
        intent.putExtra("월", month);
        intent.putExtra("일", dayOfMonth);
        startActivity(intent);
        System.out.println("Move To Modify Diary");
    }

    public void showDeleteDialog(View view){    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryContent.this);
        dialog.setMessage("일기를 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(DiaryContent.this,"네 클릭", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(DiaryContent.this,"아니오 클릭", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void goToBack(View view){   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }

    // Firebase에서 일기 조회
    public void getData(int year,int month,int dayOfMonth){
        database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("/일기장/아이디/"+year+month+dayOfMonth);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    Diary value = dataSnapshot.getValue(Diary.class);
                    System.out.println(year+""+month+""+dayOfMonth +" 일기조회");
                    System.out.println("Title: " + value.title);
                    System.out.println("Content: " + value.content);
                    System.out.println("EmoticonNum: " + value.emoticonNum);
                }catch (Exception e){
                    System.out.println("일기 없음");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value."+ error.toException());
            }
        });
    }
}