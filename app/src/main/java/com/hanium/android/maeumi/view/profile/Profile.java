package com.hanium.android.maeumi.view.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.R;

public class Profile extends Activity {

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
}
