package com.hanium.android.maeumi.view.heartprogram;

import android.os.Bundle;
import android.view.View;
import com.hanium.android.maeumi.R;
import androidx.appcompat.app.AppCompatActivity;

public class HeartGuide extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_guide);
    }

    public void goToBack(View view){
        finish();
    }
}
