package com.hanium.android.maeumi.view.selftest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.TestModel;

public class TestHistory extends AppCompatActivity {

    public static String victimScore;
    public static String perpetrationScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);

        TestModel TestModel = new TestModel();

        TextView victimScoreText = findViewById(R.id.victimScore);
        TextView perpetrationScoreText = findViewById(R.id.perpetrationScore);


        victimScore = TestModel.getVictimScore();
        perpetrationScore = TestModel.getPerpetrationScore();

        victimScoreText.setText(victimScore);
        perpetrationScoreText.setText(perpetrationScore);

    }


    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}