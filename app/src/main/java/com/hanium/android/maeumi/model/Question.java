package com.hanium.android.maeumi.model;

public class Question {
    private int num;    //문항 번호
    private String content; //내용
    private boolean isChecked;  //사용자가 답변을 선택했는지 여부
    private int selectedOption; //사용자가 선택한 답변 번호

    public Question() {

    }

    public Question(int num, String content) {
        this.num = num;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}
