package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivitySplash extends AppCompatActivity {
    private static final String TAG = "splash";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);   //로딩 화면

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) { //로그인 기록이 있는 사용자
                    Log.d(TAG, "로그인 계정 : " + user.getUid());
                    startActivity(new Intent(ActivitySplash.this, MainActivity.class)); //메인 화면으로 이동
                } else {    //로그인 기록이 없는 사용자
                    startActivity(new Intent(ActivitySplash.this, LoginActivity.class));    //로그인 화면으로 이동
                }
                finish();
            }
        }, 2500);

        /*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                System.out.println("Splash Splash");
            }
        }, 3000);   //3초 뒤 메인으로 이동
         */
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
