package com.hanium.android.maeumi.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hanium.android.maeumi.R;

public class Board extends AppCompatActivity {
    private final int fragment_1 = 1; //자유게시판
    private final int fragment_2 = 2; //익명게시판
    private int cur_fragment;   //현재 fragment 번호

    TextView boardNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);
        boardNameText = findViewById(R.id.boardNameText);

        //게시판 버튼 이벤트 리스너 추가
        findViewById(R.id.freeBoardBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentView(fragment_1);
            }
        });

        findViewById(R.id.anonymousBoardBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentView(fragment_2);
            }
        });

        cur_fragment = fragment_1;
        FragmentView(cur_fragment);   //처음 화면은 자유게시판
        boardNameText.setText("자유게시판");
    }

    //fragment 부착
    private void FragmentView(int fragment) {
        //FragmentTransactiom를 이용해 fragment 사용
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
            case 1:
                // 첫번째 fragment(자유게시판) 호출
                BoardFreeFragment freeFragment = new BoardFreeFragment();
                transaction.replace(R.id.board_fragment_container, freeFragment);
                transaction.commit();
                cur_fragment = fragment_1;  //현재 fragment 번호 업데이트
                boardNameText.setText("자유게시판");
                break;

            case 2:
                // 두번째 fragment(익명게시판) 호출
                BoardAnonymousFragment anonymousFragment = new BoardAnonymousFragment();
                transaction.replace(R.id.board_fragment_container, anonymousFragment);
                transaction.commit();
                cur_fragment = fragment_2; //현재 fragment 번호 업데이트
                boardNameText.setText("익명게시판");
                break;
        }
    }

    /*
    public void goToBoardAnonymous(View view){
        Intent intent = new Intent(Board.this, BoardAnonymous.class);
        startActivity(intent);
        System.out.println("익명게시판으로 이동");
    }
    */

    public void goToBoardWrite(View view) {
        Intent intent = new Intent(Board.this, BoardWrite.class);
        startActivity(intent);
        System.out.println("자유게시판 게시글 작성");
    }

    //정렬 클릭
    public void onFilterClick(final View view) {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.board_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_menu1) {
                    Toast.makeText(Board.this, "메뉴 1 클릭", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.action_menu2) {
                    Toast.makeText(Board.this, "메뉴 2 클릭", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Board.this, "메뉴 3 클릭", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        popupMenu.show();
    }
}
