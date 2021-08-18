package com.hanium.android.maeumi.view.guide;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

public class Guide extends AppCompatActivity {

    int i = 0;
    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Button button =(Button) findViewById(R.id.guideButton);
        imageView = (ImageView)findViewById(R.id.guide1ImageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0;i<=18;i++){
                    if (i == 0){
                        imageView.setImageResource(R.drawable.guide1);
                    }
                    else if (i == 1){
                        imageView.setImageResource(R.drawable.guide2);
                    }
                    else {
                        imageView.setImageResource(R.drawable.guide3);
                    }
                }
            }
        });

    }
    public void goToBack(View view){
        finish();
    }
}