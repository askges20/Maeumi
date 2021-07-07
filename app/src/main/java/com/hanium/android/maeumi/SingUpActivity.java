package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SingUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
    }
    public void onSignUpClick(View view){
        Intent intent = new Intent(SingUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}