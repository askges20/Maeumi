package com.hanium.android.maeumi.view.loading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.MainActivity;
import com.hanium.android.maeumi.R;

public class ActivitySplash extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference userRef;
    FirebaseAuth firebaseAuth;
    private static final String TAG = "splash";

    FirebaseUser userFromFB;
    ImageView splashImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //다크모드 지원 X

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);   //로딩 화면

        splashImgView =findViewById(R.id.splashImgView);
        Glide.with(getApplicationContext()).load(R.drawable.maeumi_splash).into(splashImgView);



        userFromFB = FirebaseAuth.getInstance().getCurrentUser();  //사용자가 이전에 로그인한 계정

        if (userFromFB != null) {    //이전에 로그인을 했었다면 DB에서 정보 조회, 저장
            String loginUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //로그인한 계정의 uid
            LoginUser user = LoginUser.getInstance();    //싱글톤 패턴
            user.setUid(loginUserUid);  //사용자 uid

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

                    System.out.println("사용자 정보 저장");
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

            user.setHistory(this);  //사용자 테스트 결과 DB에서 읽고 저장하기 (완료되면 moveToMain 실행)

        } else {    //이전에 로그인을 한 기록이 없으면
            startActivity(new Intent(ActivitySplash.this, LoginActivity.class));
            finish();   //로그인 화면으로 이동
        }
    }

    //메인 화면으로 이동하기
    public void moveToMain() {
        firebaseAuth.getInstance().getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (userFromFB.isEmailVerified()) {
                    startActivity(new Intent(ActivitySplash.this, MainActivity.class)); //메인 화면으로 이동
                    Log.d(TAG, "로그인 계정 uid : " + userFromFB.getUid());
                    finish();   //현재 액티비티(로그인 화면) 종료
                } else {
                    Toast.makeText(ActivitySplash.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ActivitySplash.this, LoginActivity.class));    //로그인 화면으로 이동
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}