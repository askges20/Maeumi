package com.hanium.android.maeumi.view.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

public class ProfileEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        final EditText name = (EditText)findViewById(R.id.name);
        final EditText school =(EditText)findViewById(R.id.school);
        final EditText gender = (EditText)findViewById(R.id.gender);
        final EditText phoneNumber =(EditText)findViewById(R.id.phoneNumber);
        final EditText alias =(EditText)findViewById(R.id.alias);
        Button saveBtn = (Button)findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(ProfileEdit.this, "변경완료", Toast.LENGTH_SHORT).show();

                if(name.getText().toString().equals("")){
                    System.out.println("Nothing Changed.");
                }else{
                    System.out.println("이름 변경 "+name.getText());
                }

                if(school.getText().toString().equals("")){
                    System.out.println("Nothing Changed.");
                }else{
                    System.out.println("학교 변경 "+school.getText());
                }

                if(gender.getText().toString().equals("")){
                    System.out.println("Nothing Changed.");
                }else{
                    System.out.println("성별 변경 "+gender.getText());
                }

                if(phoneNumber.getText().toString().equals("")){
                    System.out.println("Nothing Changed.");
                }else{
                    System.out.println("전화번호 변경 "+phoneNumber.getText());
                }

                if(alias.getText().toString().equals("")){
                    System.out.println("Nothing Changed.");
                }else{
                    System.out.println("닉네임 변경 "+alias.getText());
                }
                finish();
            }
        });

    }
}
