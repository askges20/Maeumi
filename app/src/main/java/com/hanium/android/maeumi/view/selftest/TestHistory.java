package com.hanium.android.maeumi.view.selftest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.TestModel;

public class TestHistory extends AppCompatActivity {

    public static String victimScore;
    public static String perpetrationScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);

        TextView victimScoreText = findViewById(R.id.victimScore);
        TextView perpetrationScoreText = findViewById(R.id.perpetrationScore);

        victimScore = LoginUser.getInstance().getVictimScore();
        perpetrationScore = LoginUser.getInstance().getPerpetrationScore();

        victimScoreText.setText(victimScore);
        perpetrationScoreText.setText(perpetrationScore);

        if (victimScore.equals("null")){
            alertNoResult();
        }
    }

    //테스트 결과가 존재하지 않음을 알림
    public void alertNoResult() {
        Toast.makeText(this, "테스트 결과가 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}