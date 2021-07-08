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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity {

    EditText email, password, name;
    FirebaseAuth firebaseAuth;
    private String userEmail, userPassword, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        name = findViewById(R.id.userName);

    }
    public void onSignUpClick(View view){

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        userName = name.getText().toString();

        if (userEmail.equals("") || userPassword.equals("") || userName.equals("")){
            Toast.makeText(SingUpActivity.this,"빈 칸을 채워주세요.",Toast.LENGTH_LONG).show();
        }else{
            createUser(userEmail,userPassword,userName);

        }
    }

    private void createUser(String email, String password, String userName) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( SingUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = userName;

                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("uid",uid);
                            hashMap.put("email",email);
                            hashMap.put("name",name);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(SingUpActivity.this,"회원가입 성공",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SingUpActivity.this,LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(SingUpActivity.this,"동일한 아이디가 존재합니다.",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}