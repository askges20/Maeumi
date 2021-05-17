package com.hanium.android.maeumi.view.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hanium.android.maeumi.R;

public class Profile extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
    }

    public void goToProfileEdit(View view){
        Intent intent = new Intent(Profile.this,ProfileEdit.class);
        startActivity(intent);
        System.out.println("Move To Profile Edit");
    }
}
