package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SingUpActivity extends AppCompatActivity {

    EditText email, password, name, alias, school;
    FirebaseAuth firebaseAuth;
    private String userEmail, userPassword, userName, userGender, userAlias, userSchool;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        name = findViewById(R.id.userName);
        alias = findViewById(R.id.alias);
        school = findViewById(R.id.school);
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.girl) {
                    userGender = "여";
                } else if (checkedId == R.id.man) {
                    userGender = "남";
                }
            }
        });

    }

    public void onSignUpClick(View view) {

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        userName = name.getText().toString();
        userAlias = alias.getText().toString();
        userSchool = school.getText().toString();

        Pattern pattern = Patterns.EMAIL_ADDRESS;

        if (!pattern.matcher(userEmail).matches()) {
            Toast.makeText(SingUpActivity.this, "이메일 형식을 맞춰주세요", Toast.LENGTH_LONG).show();
        } else if (!Pattern.matches("^(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", userPassword)) {
            Toast.makeText(SingUpActivity.this, "8자 이상, 숫자, 특수문자가 각각 1개이상 입력해주세요.", Toast.LENGTH_LONG).show();
        }else if (userName.equals("")) {
            Toast.makeText(SingUpActivity.this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
        } else if (userGender == null) {
            Toast.makeText(SingUpActivity.this, "성별을 선택해주세요.", Toast.LENGTH_LONG).show();
        } else if (userAlias == null) {
            Toast.makeText(SingUpActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
        } else if (userSchool == null) {
            Toast.makeText(SingUpActivity.this, "학교를 입력해주세요.", Toast.LENGTH_LONG).show();
        } else {
            createUser(userEmail, userPassword, userName, userGender, userAlias, userSchool);

        }
    }
    private void createUser(String email, String password, String userName, String UserGender, String userAlias, String userSchool) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SingUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = userName;
                            String gender = UserGender;
                            String alias = userAlias;
                            String school = userSchool;

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("name", name);
                            hashMap.put("gender", gender);
                            hashMap.put("alias", alias);
                            hashMap.put("school", school);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(SingUpActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(SingUpActivity.this, "동일한 아이디가 존재합니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}