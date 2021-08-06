package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivitySplash extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference userRef;
    private static final String TAG = "splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //다크모드 지원 X

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);   //로딩 화면


        FirebaseUser userFromFB = FirebaseAuth.getInstance().getCurrentUser();  //사용자가 이전에 로그인한 계정

        if(userFromFB != null) {    //이전에 로그인을 했었다면 DB에서 정보 조회, 저장
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
//                    user.setHeart(snapshot.child("heart").getValue(int.class)); //마음 채우기 정도
                    if(snapshot.child("heart").getValue(String.class) == null){
                        user.setHeart("-1");
                    }else{
                        user.setHeart(snapshot.child("heart").getValue(String.class));
                    }

                    System.out.println("사용자 정보 저장");
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

            user.setHistory();  //사용자 테스트 결과 DB에서 읽고 저장하기
        }


        //스레드 실행
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userFromFB != null) { //로그인 기록이 있는 사용자
                    Log.d(TAG, "로그인 계정 uid : " + userFromFB.getUid());
                    startActivity(new Intent(ActivitySplash.this, MainActivity.class)); //메인 화면으로 이동
                } else {    //로그인 기록이 없는 사용자
                    startActivity(new Intent(ActivitySplash.this, LoginActivity.class));    //로그인 화면으로 이동
                }
                finish();
            }
        }, 4500);   //4초 뒤 이동
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
