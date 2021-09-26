package com.hanium.android.maeumi.view.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hanium.android.maeumi.view.heartprogram.HeartProgram;
import com.hanium.android.maeumi.view.loading.LoginActivity;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;

public class Profile extends AppCompatActivity {

    Button goToHeartProgramBtn;
    TextView userName, userEmail, userAlias, userSchool, userGender, userHeartText;
    ImageView heartProgress, logoutBtn;
    private String name, email, alias, gender, school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        LoginUser loginUser = LoginUser.getInstance();

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userAlias = findViewById(R.id.userAlias);
        userSchool = findViewById(R.id.userSchool);
        userGender = findViewById(R.id.userGender);
        userHeartText = findViewById(R.id.userHeartText);
        heartProgress = findViewById(R.id.heartProgress);

        name = loginUser.getName();
        email = loginUser.getEmail();
        alias = loginUser.getAlias();
        school = loginUser.getSchool();
        gender = loginUser.getGender();

        userName.setText(name);
        userEmail.setText(email);
        userAlias.setText(alias);
        userSchool.setText(school);
        userGender.setText(gender);

        setHeartProgress(); //마음 온도에 따라 하트 이미지 출력

        //로그아웃 버튼
        logoutBtn = findViewById(R.id.signOutBtn);
    }

    //마음 온도에 따라 하트 이미지 설정
    public void setHeartProgress() {
        int heart = Integer.parseInt(LoginUser.getInstance().getHeart());
        if (heart < 10){
            heartProgress.setImageResource(R.drawable.z_heart_00);
        } else if (heart < 20){
            heartProgress.setImageResource(R.drawable.z_heart_01);
        } else if (heart < 30){
            heartProgress.setImageResource(R.drawable.z_heart_02);
        }else if (heart < 40){
            heartProgress.setImageResource(R.drawable.z_heart_03);
        }else if (heart < 50){
            heartProgress.setImageResource(R.drawable.z_heart_04);
        }else if (heart < 60){
            heartProgress.setImageResource(R.drawable.z_heart_05);
        }else if (heart < 70){
            heartProgress.setImageResource(R.drawable.z_heart_06);
        }else if (heart < 80){
            heartProgress.setImageResource(R.drawable.z_heart_07);
        }else if (heart < 90){
            heartProgress.setImageResource(R.drawable.z_heart_08);
        }else if (heart < 100){
            heartProgress.setImageResource(R.drawable.z_heart_09);
        }else {
            heartProgress.setImageResource(R.drawable.z_heart_10);
        }

        if (heart == -1) {
            userHeartText.setText("아직 마음 온도가 없어요!\n진단테스트를 진행해주세요");
        } else {
            userHeartText.setText(heart + " / 100");
        }
    }

    //로그아웃 버튼 클릭 이벤트
    public void signOutBtnEvent(View view) {
        new AlertDialog.Builder(this)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //네 클릭
                        processSignOut();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //취소 클릭
                    }
                })
                .show();
    }

    public void goToHeartProgram(View view){
        Intent intent = new Intent(this, HeartProgram.class);
        startActivity(intent);
        finish();
    }

    //로그아웃 진행
    public void processSignOut() {
        LoginUser.signOutUser();    //싱글톤 객체 new
        FirebaseAuth.getInstance().signOut();   //FirebaseAuth에서 로그아웃

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);  //로그인 화면으로 이동
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}
