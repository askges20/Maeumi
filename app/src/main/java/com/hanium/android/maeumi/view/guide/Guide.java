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

        Button button = (Button)findViewById(R.id.guideButton);
        imageView = (ImageView)findViewById(R.id.guide1ImageView);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                i = 1 -i;

                if(i == 0){
                    imageView.setImageResource(R.drawable.guide1);
                }
                else{
                    imageView.setImageResource(R.drawable.guide2);
                }
            }
        });


    }
    public void goToBack(View view){
        finish();
    }
}