package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth firebaseAuth;
    private String userEmail, userPassword;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
    }

    public void onLoginClick(View view){
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if (userEmail.equals("") || userPassword.equals("")){
            Toast.makeText(LoginActivity.this,"이메일과 비밀번호를 입력하세요.",Toast.LENGTH_LONG).show();
        }else{
            login(userEmail,userPassword);

        }

        // 뭐 하는지 모르는데 예제에 있어서 추가했어요
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };

    }

    public void onSignUpClick(View view){
        Intent intent = new Intent(LoginActivity.this,SingUpActivity.class);
        startActivity(intent);
    }

    public void login(String email, String password){
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"아이디 혹은 비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}