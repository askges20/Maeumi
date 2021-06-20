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

public class TestClick extends AppCompatActivity {

    private Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testclick);

        RecyclerView recyclerView = findViewById(R.id.testRecyclerView);

        //레이아웃 매니저로 배치 방법 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //어댑터를 이용해서 리사이클러뷰에 데이터 넘김
        //추후 firebase 데이터와 연결할 것
        QuestionAdapter adapter = new QuestionAdapter();
        adapter.addItem(new Question(1,"나는 학교에서 다른 아이들로부터 위협이나 협박을 당한 적이 있다."));
        adapter.addItem(new Question(2,"나는 다른 아이들로부터 이유없이 신체적으로 구타를 당한 적이 있다."));
        adapter.addItem(new Question(3,"나는 학교에서 친한 친구가 없다."));
        adapter.addItem(new Question(4,"나는 학교에서 다른 아이들과 잘 어울리지 못한다."));
        adapter.addItem(new Question(5,"나는 학교에서 다른 아이들로부터 강제로 돈을 빼앗긴 적이 있다."));
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
}