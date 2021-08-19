package com.hanium.android.maeumi.view.board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.hanium.android.maeumi.R;

public class Board extends AppCompatActivity {
    private final String fragment_free = "free"; //자유게시판
    private final String fragment_question = "question";   //질문&고민 게시판
    private final String fragment_tip = "tip";    //꿀팁 게시판
    private final String fragment_anonymous = "anonymous"; //익명게시판
    private String cur_fragment;   //현재 fragment

    TextView boardNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);
        boardNameText = findViewById(R.id.boardNameText);

        //게시판 카테고리 탭 메뉴
        TabLayout tabLayout = findViewById(R.id.boardTabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch(pos){
                    case 0:
                        setFragmentView(fragment_free);
                        break;
                    case 1:
                        setFragmentView(fragment_question);
                        break;
                    case 2:
                        setFragmentView(fragment_tip);
                        break;
                    case 3:
                        setFragmentView(fragment_anonymous);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cur_fragment = fragment_free;
        setFragmentView(cur_fragment);   //처음 화면은 자유게시판
        boardNameText.setText("자유게시판");
    }

    //fragment 부착
    private void setFragmentView(String fragment) {
        //FragmentTransaction를 이용해 fragment 사용
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
            case "free":
                // 자유게시판 fragment
                BoardFreeFragment freeFragment = new BoardFreeFragment(this);
                transaction.replace(R.id.board_fragment_container, freeFragment);
                transaction.commit();
                cur_fragment = fragment_free;  //현재 fragment 번호 업데이트
                boardNameText.setText("자유게시판");
                break;

            case "question":
                //  질문게시판 fragment
                BoardQuestionFragment questionFragment = new BoardQuestionFragment(this);
                transaction.replace(R.id.board_fragment_container, questionFragment);
                transaction.commit();
                cur_fragment = fragment_question;  //현재 fragment 번호 업데이트
                boardNameText.setText("질문게시판");
                break;

            case "tip":
                //  꿀팁게시판 fragment
                BoardTipFragment tipFragment = new BoardTipFragment(this);
                transaction.replace(R.id.board_fragment_container, tipFragment);
                transaction.commit();
                cur_fragment = fragment_tip;  //현재 fragment 번호 업데이트
                boardNameText.setText("꿀팁게시판");
                break;

            case "anonymous":
                //  익명게시판 fragment
                BoardAnonymousFragment anonymousFragment = new BoardAnonymousFragment(this);
                transaction.replace(R.id.board_fragment_container, anonymousFragment);
                transaction.commit();
                cur_fragment = fragment_anonymous; //현재 fragment 번호 업데이트
                boardNameText.setText("익명게시판");
                break;
        }
    }

    public void goToBoardWrite(View view) {
        Intent intent = new Intent(Board.this, BoardWrite.class);
        intent.putExtra("타입",cur_fragment); //어떤 게시판에 작성할건지 전달
        startActivity(intent);
    }

    public void showBoardGuide(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
        .setTitle("게시판 이용 안내")
        .setMessage(R.string.board_guide)
        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        }).show();

        TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
        dialogMessage.setTextSize(18);
    }

    public void goToBack(View view) {
        finish();
    }
}
