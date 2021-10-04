package com.hanium.android.maeumi.view.loading;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.MainActivity;
import com.hanium.android.maeumi.R;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference userRef;

    EditText email, password;
    FirebaseAuth firebaseAuth;
    FirebaseUser mUser;
    private String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);

    }

    public void onLoginClick(View view) {
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if (userEmail.equals("") || userPassword.equals("")) {
            Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
        } else {
            login(userEmail, userPassword);

        }

    }

    public void onSignUpClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);  //회원가입 화면으로 이동
    }

    public void login(String email, String password) {
        AlertDialog.Builder helpPopup = new AlertDialog.Builder(LoginActivity.this);
        AlertDialog ad = helpPopup.create();
        ad.setTitle("로그인");
        ad.setIcon(R.drawable.maeumi_main_img);
        ad.setMessage("잠시만 기다려주세요.");
        ad.setCancelable(false);

        ad.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        ad.dismiss();
                        String loginUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //로그인한 계정의 uid
                        LoginUser user = LoginUser.getInstance();    //싱글톤 패턴
                        user.setUid(loginUserUid);  //사용자 uid
                        mUser = firebaseAuth.getInstance().getCurrentUser();

                        //DB에서 사용자 정보 가져와서 싱글톤 객체에 저장
                        database = FirebaseDatabase.getInstance();
                        userRef = database.getReference("/Users/" + loginUserUid + "/");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                user.setName(snapshot.child("name").getValue(String.class)); //사용자 이름
                                user.setEmail(snapshot.child("email").getValue(String.class));  //사용자 이메일
                                user.setAlias(snapshot.child("alias").getValue(String.class));
                                user.setGender(snapshot.child("gender").getValue(String.class));
                                user.setSchool(snapshot.child("school").getValue(String.class));
                                user.setNotifyDate(snapshot.child("notifyDate").getValue(String.class));
                                if (snapshot.child("heart").getValue(String.class) == null) {
                                    user.setHeart("-1");
                                } else {
                                    user.setHeart(snapshot.child("heart").getValue(String.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                        if (mUser != null) {
                            firebaseAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (mUser.isEmailVerified()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class); //메인 화면으로 이동
                                        startActivity(intent);
                                        finish();   //현재 액티비티(로그인 화면) 종료
                                    } else {
                                        Toast.makeText(LoginActivity.this, "이메일 인증 후 로그인 해주세요.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    } else {
                        ad.dismiss();
                        Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
            }
        });
    }

    @Override
    public void onBackPressed() {   //뒤로가기 버튼 클릭 시

        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setMessage("앱을 종료하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        dialog.show();
    }

    //네트워크 연결 여부 리턴
    public boolean isConnect2Network() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo.State networkState = manager.getActiveNetworkInfo().getState();
        if (networkState == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return true;
    }

}