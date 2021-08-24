package com.hanium.android.maeumi.view.guide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.MainActivity;
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
                    Toast.makeText(Guide.this,"첫 페이지 입니다.",Toast.LENGTH_SHORT).show();
                } else {    //마지막 페이지로 이동
                    i--;
                }
                imageView.setImageResource(typedArray.getResourceId(i, -1));
            }
        });
        Button right_btn = (Button) findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == max-1) { //맨 마지막 페이지일 때
                    donePopUp();
                }else{
                    i++;
                }
                imageView.setImageResource(typedArray.getResourceId(i, -1));
            }
        });

    }
    public void donePopUp() {
        AlertDialog.Builder helpPopup = new AlertDialog.Builder(Guide.this);
        helpPopup.setTitle("이용안내 완료");
        helpPopup.setIcon(R.drawable.maeumi_main_img);
        helpPopup.setMessage("본격적으로 마음이 앱을 이용해보세요!");
        helpPopup.setCancelable(false);

        helpPopup.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },300);
            }
        });
        helpPopup.show();
    }
    public void goToBack(View view) {
        finish();
    }
}