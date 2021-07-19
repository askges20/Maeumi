package com.hanium.android.maeumi.view.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.hanium.android.maeumi.LoginActivity;
import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.MainActivity;
import com.hanium.android.maeumi.R;

public class Profile extends AppCompatActivity {

    Button logoutBtn;
    TextView userName, userEmail, userAlias, userSchool, userGender;
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

        //로그아웃 버튼
        logoutBtn = findViewById(R.id.signOutBtn);
    }

    public void goToProfileEdit(View view) {
        Intent intent = new Intent(Profile.this, ProfileEdit.class);
        startActivity(intent);
    }

    public void deleteConfirm(View view) {
        new AlertDialog.Builder(this)
                .setTitle("게시글 삭제")
                .setMessage("게시글을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Profile.this, "삭제완료", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Profile.this, "취소완료", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    //로그아웃 버튼 클릭 이벤트
    public void signOutBtnEvent(View view) {
        new AlertDialog.Builder(this)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
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
