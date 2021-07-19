package com.hanium.android.maeumi.view.selftest;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Question;
import com.hanium.android.maeumi.adapters.QuestionAdapter;
import com.hanium.android.maeumi.viewmodel.TestViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TestClick extends AppCompatActivity {

    private Button finishBtn;
    private ArrayList<Question> questionList = new ArrayList<Question>();

    ListView questionListView;
    ImageView maeumiImg;
    ProgressBar progressBar;
    double progress = 0;

    TestViewModel testViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testclick);

        questionListView = findViewById(R.id.questionListView);
        maeumiImg = findViewById(R.id.maeumiImg);
        progressBar = findViewById(R.id.testProgressBar);

        String jsonString = getJsonString();    //json 파일 읽기
        jsonParsing(jsonString);    //json 파싱 -> ArrayList에 담기

        //어댑터를 이용해서 리스트뷰에 데이터 넘김
        QuestionAdapter adapter = new QuestionAdapter(this, this);
        adapter.setItems(questionList);
        questionListView.setAdapter(adapter);   //어댑터 등록

        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isAllChecked()) {    //모든 문항에 답변을 했으면
                    testViewModel = new TestViewModel(adapter); //TestViewModel에서 결과 계산, DB 반영

                    finish();   //현재 액티비티 종료
                    Intent intent = new Intent(TestClick.this, TestResult.class);
                    intent.putExtra("victim", testViewModel.getVictimValue());
                    intent.putExtra("perpetration", testViewModel.getPerpetrationValue());
                    startActivity(intent); //액티비티 이동
                } else {
                    Toast.makeText(TestClick.this, "체크하지 않은 문항이 있습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //테스트 문항 json 파일 읽어오기
    private String getJsonString() {
        String json = "";

        try {
            InputStream is = getAssets().open("questions.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray questionArray = jsonObject.getJSONArray("Questions");

            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject questionObject = questionArray.getJSONObject(i);
                Question question = new Question();
                question.setNum(questionObject.getInt("num"));
                question.setContent(questionObject.getString("content"));

                questionList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //테스트 진행 정도 업데이트
    public void changeProgress(int cntChecked, int total) {
        double progressGoal = (double) cntChecked / (double) total * 100;
        System.out.println("진행 정도 : " + progress);

        //프로그레스바 반영
        final Timer t = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                progress += 0.1;
                if (progress >= progressGoal){
                    progress = progressGoal;
                    t.cancel();
                }
                progressBar.setProgress((int)progress);
            }
        };

        t.schedule(timerTask, 0, 4);

        //이미지 이동 애니메이션
        TranslateAnimation ani = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, (float)(cntChecked - 1) * 0.39f,
                Animation.RELATIVE_TO_SELF, (float)cntChecked * 0.39f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        ani.setFillAfter(true);
        ani.setDuration(500);
        maeumiImg.startAnimation(ani);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}