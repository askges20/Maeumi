package com.hanium.android.maeumi.view.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Question;
import com.hanium.android.maeumi.adapters.QuestionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TestClick extends AppCompatActivity {

    private Button finishBtn;
    private ArrayList<Question> questionList = new ArrayList<Question>();

    ListView questionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testclick);

        questionListView = findViewById(R.id.questionListView);

        String jsonString = getJsonString();    //json 파일 읽기
        jsonParsing(jsonString);    //json 파싱 -> ArrayList에 담기

        //어댑터를 이용해서 리스트뷰에 데이터 넘김
        QuestionAdapter adapter = new QuestionAdapter(this);
        adapter.setItems(questionList);
        questionListView.setAdapter(adapter);   //어댑터 등록

        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isAllChecked()){    //모든 문항에 답변을 했으면
                    Intent intent = new Intent(TestClick.this, TestResult.class);
                    finish();   //현재 액티비티 종료
                    startActivity(intent); //액티비티 이동
                } else {
                    Toast.makeText(TestClick.this, "체크하지 않은 문항이 있습니다!", Toast.LENGTH_SHORT).show();
                }
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