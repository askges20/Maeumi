package com.hanium.android.maeumi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Comment;
import com.hanium.android.maeumi.model.Question;

import java.util.ArrayList;
import java.util.HashSet;

public class QuestionAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<Question> items = new ArrayList<Question>();
    HashSet<Integer> checkedQuestions = new HashSet<Integer>(); //답변한 문항 번호

    public QuestionAdapter(Context context) {
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
                        selectedNum = 1;
                        break;
                    case R.id.option2:
                        selectedNum = 2;
                        break;
                    case R.id.option3:
                        selectedNum = 3;
                        break;
                    case R.id.option4:
                        selectedNum = 4;
                        break;
                }
                setSelectedNum(position, selectedNum);  //선택 반영
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
}
