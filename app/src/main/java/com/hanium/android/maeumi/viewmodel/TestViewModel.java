package com.hanium.android.maeumi.viewmodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.adapters.QuestionAdapter;
import com.hanium.android.maeumi.model.Question;

import java.util.ArrayList;

public class TestViewModel {

    FirebaseDatabase firebase;
    DatabaseReference testReference;

    QuestionAdapter adapter;
    ArrayList<Question> questions;

    String answers = ""; //체크한 답변을 문자열로 저장
    int victimValue = 0;    //피해 정도
    int perpetrationValue = 0;  //가해 정도

    public TestViewModel(QuestionAdapter questionAdapter) {
        this.adapter = questionAdapter;
        questions = this.adapter.getItems();

        firebase = FirebaseDatabase.getInstance();
        testReference = firebase.getReference("/진단테스트/" + LoginUser.getInstance().getUid() + "/");

        calSelectedOptions();   //테스트 결과 계산
        resultToFirebase(); //DB에 저장
    }

    //테스트 결과 계산
    private void calSelectedOptions() {
        int optionNum;
        for (int i = 0; i <= 11; i++) {
            optionNum = questions.get(i).getSelectedOption();
            victimValue += optionNum;    //피해 정도 합산
            answers += optionNum;   //문자열 마지막에 추가
        }
        for (int j = 12; j < questions.size(); j++) {
            optionNum = questions.get(j).getSelectedOption();
            perpetrationValue += optionNum;  //가해 정도 합산
            answers += optionNum;   //문자열 마지막에 추가
        }
    }

    //테스트 결과 firebase DB 반영 부분
    private void resultToFirebase() {
        testReference.child("답변").setValue(answers);
        testReference.child("피해정도").setValue(victimValue);
        testReference.child("가해정도").setValue(perpetrationValue);
    }

    //피해 정도 리턴
    public int getVictimValue(){
        return victimValue;
    }

    //가해 정도 리턴
    public int getPerpetrationValue(){
        return perpetrationValue;
    }
}
