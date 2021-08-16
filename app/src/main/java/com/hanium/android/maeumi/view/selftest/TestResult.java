package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.view.heartprogram.HeartGuide;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;

public class TestResult extends AppCompatActivity {

    int victimScore = -1;    //피해 정도
    int perpetrationScore = -1;  //가해 정도

    ProgressBar victimProgress;
    ProgressBar perpetrationProgress;

    TextView victimResultDetailText, victimScoreText;
    TextView perpetrationResultDetailText, perpetrationScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        System.out.println("테스트 결과 화면");

        victimScoreText = findViewById(R.id.victimScore);
        perpetrationScoreText = findViewById(R.id.perpetrationScore);

        victimScore = getIntent().getIntExtra("victim", -1);    //테스트 진행 화면에서 넘어왔으면 intent로 받기
        if (victimScore == -1) {    //이전 테스트 결과 조회면
            victimScore = Integer.parseInt(LoginUser.getInstance().getVictimScore());
        }

        perpetrationScore = getIntent().getIntExtra("perpetration", -1);    //테스트 진행 화면에서 넘어왔으면 intent로 받기
        if (perpetrationScore == -1) {    //이전 테스트 결과 조회면
            perpetrationScore = Integer.parseInt(LoginUser.getInstance().getPerpetrationScore());
        }

        System.out.println("피해정도:"+victimScore);
        System.out.println("가해정도:"+perpetrationScore);

        //프로그레스바 나타내기
        victimProgress = findViewById(R.id.victimProgressBar);
        victimProgress.setMax(36);
        victimProgress.setMin(0);
        victimProgress.setProgress(victimScore);

        perpetrationProgress = findViewById(R.id.perpetrationProgressBar);
        perpetrationProgress.setMax(36);
        perpetrationProgress.setMin(0);
        perpetrationProgress.setProgress(perpetrationScore);


        victimResultDetailText = findViewById(R.id.victimResultDetailText);
        perpetrationResultDetailText = findViewById(R.id.perpetrationResultDetailText);

        setTestResult();    //결과 상세 내용 텍스트 반영
    }

    public void setTestResult() {
        //피해 정도 결과
        if (victimScore <= 6) {  //0~6점 아주 약함
            victimResultDetailText.setText(R.string.test_result_victim_1);
            victimScoreText.setText("아주 약함");
        } else if (victimScore <= 13) {  //7~13점 약함
            victimResultDetailText.setText(R.string.test_result_victim_2);
            victimScoreText.setText("약함");
        } else if (victimScore <= 22) {  //14~22점 보통
            victimResultDetailText.setText(R.string.test_result_victim_3);
            victimScoreText.setText("보통");
        } else if (victimScore <= 29) {  //23~29점 심함
            victimResultDetailText.setText(R.string.test_result_victim_4);
            victimScoreText.setText("심함");
        } else {    //30~36점 아주 심함
            victimResultDetailText.setText(R.string.test_result_victim_5);
            victimScoreText.setText("아주 심함");
        }

        //가해 정도 결과
        if (perpetrationScore <= 6) {
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_1);
            perpetrationScoreText.setText("아주 약함");
        } else if (perpetrationScore <= 13) {
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_2);
            perpetrationScoreText.setText("약함");
        } else if (perpetrationScore <= 22) {
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_3);
            perpetrationScoreText.setText("보통");
        } else if (perpetrationScore <= 29) {
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_4);
            perpetrationScoreText.setText("심함");
        } else {
            perpetrationResultDetailText.setText(R.string.test_result_perpetration_5);
            perpetrationScoreText.setText("아주 심함");
        }
    }

    public void goToBoard(View view) {
        Intent intent = new Intent(TestResult.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
    }

    //이전 화면으로 이동
    public void goBack(View view) {
        boolean isAfterTest = getIntent().getBooleanExtra("afterTest", false);

        if (isAfterTest) { //테스트 완료 후
            showHeartDialog(R.layout.heart_popup);  //마음 채우기 이동 팝업
        } else {    //지난 테스트 결과 조회한 경우
            finish();   //현재 액티비티 종료
        }
    }

    @Override
    public void onBackPressed() {   //뒤로가기 버튼 클릭 시
        goBack(null);
    }

    //마음 채우기 가이드 이동 팝업 띄우기
    public void showHeartDialog(int layout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        dialogBuilder.setCancelable(false); //뒤로가기 버튼 비활성화
        dialogBuilder.setView(layoutView);

        //이동하기 버튼
        TextView moveBtn = layoutView.findViewById(R.id.moveToHeartGuideBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestResult.this, HeartGuide.class);
                intent.putExtra("fromTest", "true");
                startActivity(intent);
                finish();   //현재 액티비티(테스트 결과 화면) 종료
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //모서리 둥글게
        dialog.show();

        //팝업 사이즈
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int x = (int) (size.x * 0.7f);
        int y = (int) (size.y * 0.6f);

        dialog.getWindow().setLayout(x, y);
    }

}

