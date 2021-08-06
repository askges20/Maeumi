package com.hanium.android.maeumi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Question;
import com.hanium.android.maeumi.view.selftest.TestClick;

import java.util.ArrayList;
import java.util.HashSet;

public class QuestionAdapter extends BaseAdapter {
    TestClick testClick;
    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<Question> items = new ArrayList<Question>();
    HashSet<Integer> checkedQuestions = new HashSet<Integer>(); //답변한 문항 번호

    String answers = ""; //체크한 답변을 문자열로 저장
    int victimValue = 0;    //피해 정도
    int perpetrationValue = 0;  //가해 정도


    public QuestionAdapter(TestClick testClick, Context context) {
        this.testClick = testClick;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<Question> getItems() { return items; }

    public void setItems(ArrayList<Question> items){
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.test_item, null);
        TextView questionText = view.findViewById(R.id.quesitonText);
        RadioGroup radioGroup = view.findViewById(R.id.optionGroup);
        RadioButton option1 = view.findViewById(R.id.option1);
        RadioButton option2 = view.findViewById(R.id.option2);
        RadioButton option3 = view.findViewById(R.id.option3);
        RadioButton option4 = view.findViewById(R.id.option4);

        Question question = items.get(position);
        questionText.setText(question.getNum() + ". " + question.getContent()); //문항 내용

        //리스트뷰 메모리 문제로 체크한 번호 화면에 표시해주는 코드 작성
        if (question.isChecked()){  //체크한 문항이면
            switch(question.getSelectedOption()){
                case 1:
                    option1.setChecked(true);
                    break;
                case 2:
                    option2.setChecked(true);
                    break;
                case 3:
                    option3.setChecked(true);
                    break;
                case 4:
                    option4.setChecked(true);
                    break;
            }
        } else {    //체크하지 않은 문항
            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(false);
            option4.setChecked(false);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedNum = 0;
                switch (checkedId) {
                    case R.id.option1:
                        selectedNum = 0;
                        break;
                    case R.id.option2:
                        selectedNum = 1;
                        break;
                    case R.id.option3:
                        selectedNum = 2;
                        break;
                    case R.id.option4:
                        selectedNum = 3;
                        break;
                }
                setSelectedNum(position, selectedNum);  //선택 반영
                testClick.changeProgress(checkedQuestions.size(), getCount());
            }
        });

        return view;
    }

    //선택한 옵션 반영
    public void setSelectedNum(int position, int selectedNum) {
        items.get(position).setSelectedOption(selectedNum);
        items.get(position).setChecked(true);
        checkedQuestions.add(position + 1); //해당 문항에 답변했음을 저장
        System.out.println((position + 1) + "번째 문항 선택 : " + selectedNum);
    }

    public boolean isAllChecked(){
        //모든 문항에 체크했으면
        if (checkedQuestions.size() == items.size()){
            return true;
        } else {
            System.out.println("체크한 문항 수 : " + checkedQuestions.size());
            System.out.println(checkedQuestions);
            System.out.println("전체 문항 수 : " + items.size());
            return false;
        }
    }


    //테스트 결과 계산
    public void calSelectedOptions() {
        int optionNum;
        for (int i = 0; i <= 11; i++) {
            optionNum = items.get(i).getSelectedOption();
            victimValue += optionNum;    //피해 정도 합산
            answers += optionNum;   //문자열 마지막에 추가
        }
        for (int j = 12; j < items.size(); j++) {
            optionNum = items.get(j).getSelectedOption();
            perpetrationValue += optionNum;  //가해 정도 합산
            answers += optionNum;   //문자열 마지막에 추가
        }
    }

    //테스트 결과 firebase DB 반영 부분
    public void resultToFirebase() {
        String loginUserUid = LoginUser.getInstance().getUid();

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        DatabaseReference testReference = firebase.getReference("/진단테스트/" + loginUserUid + "/");

        testReference.child("답변").setValue(answers);
        testReference.child("피해정도").setValue(victimValue);
        testReference.child("가해정도").setValue(perpetrationValue);

        int heart = 0;
        if (perpetrationValue <= 6){
            heart = 80;
        } else if (perpetrationValue <= 13){
            heart = 60;
        } else if (perpetrationValue <= 22){
            heart = 40;
        } else if (perpetrationValue <= 29){
            heart = 20;
        }

        DatabaseReference userReference = firebase.getReference("/Users/" + loginUserUid + "/");
        userReference.child("heart").setValue(heart);   //마음 온도 저장
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
