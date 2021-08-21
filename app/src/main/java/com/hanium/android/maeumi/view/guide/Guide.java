package com.hanium.android.maeumi.view.guide;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

public class Guide extends AppCompatActivity {

    int i, max;
    ImageView imageView;
    TypedArray typedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        imageView = (ImageView) findViewById(R.id.guideImg);
        typedArray = getResources().obtainTypedArray(R.array.guide);

        i = 0;
        max = typedArray.length();
        imageView.setImageResource(typedArray.getResourceId(i, -1));

        Button left_btn = (Button) findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i <= 0) {   //맨 첫페이지에서 이전 버튼 클릭
                    i = max - 1;
                } else {    //마지막 페이지로 이동
                    i--;
                }
                System.out.println("페이지:" + i);
                imageView.setImageResource(typedArray.getResourceId(i, -1));
            }
        });
        Button right_btn = (Button) findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i >= max) { //맨 마지막 페이지일 때
                    i = 0;  //맨 첫페이지로 이동
                }
                System.out.println("페이지:" + i);
                imageView.setImageResource(typedArray.getResourceId(i, -1));
            }
        });

    }

    public void goToBack(View view) {
        finish();
    }
}