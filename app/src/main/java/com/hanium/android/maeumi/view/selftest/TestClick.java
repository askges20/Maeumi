package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Question;
import com.hanium.android.maeumi.viewmodel.QuestionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TestClick extends AppCompatActivity {

    private Button finishBtn;
    private ArrayList<Question> questionList = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testclick);

        RecyclerView recyclerView = findViewById(R.id.testRecyclerView);
        //레이아웃 매니저로 배치 방법 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        String jsonString = getJsonString();    //json 파일 읽기
        jsonParsing(jsonString);    //json 파싱 -> ArrayList에 담기

        //어댑터를 이용해서 리사이클러뷰에 데이터 넘김
        //추후 firebase 데이터와 연결할 것
        QuestionAdapter adapter = new QuestionAdapter();
        for (Question q: questionList){
            adapter.addItem(q);
        }
        /*
        adapter.addItem(new Question(1,"나는 학교에서 다른 아이들로부터 위협이나 협박을 당한 적이 있다."));
        adapter.addItem(new Question(2,"나는 다른 아이들로부터 이유없이 신체적으로 구타를 당한 적이 있다."));
        adapter.addItem(new Question(3,"나는 학교에서 친한 친구가 없다."));
        adapter.addItem(new Question(4,"나는 학교에서 다른 아이들과 잘 어울리지 못한다."));
        adapter.addItem(new Question(5,"나는 학교에서 다른 아이들로부터 강제로 돈을 빼앗긴 적이 있다."));
        */

        recyclerView.setAdapter(adapter);

        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestClick.this, TestResult.class);
                startActivity(intent); //액티비티 이동
            }
        });
    }

    //테스트 문항 json 파일 읽어오기
    private String getJsonString(){
        String json = "";

        try{
            InputStream is = getAssets().open("questions.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return json;
    }

    private void jsonParsing(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray questionArray = jsonObject.getJSONArray("Questions");

            for(int i=0; i<questionArray.length(); i++){
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
}