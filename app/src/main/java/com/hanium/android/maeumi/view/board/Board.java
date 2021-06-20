package com.hanium.android.maeumi.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;
import com.hanium.android.maeumi.viewmodel.PostAdapter;

public class Board extends AppCompatActivity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);

        RecyclerView recyclerView = findViewById(R.id.boardRecyclerView);

        //레이아웃 매니저로 배치 방법 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //어댑터를 이용해서 리사이클러뷰에 데이터 넘김
        //추후 firebase 데이터와 연결할 것
        PostAdapter adapter = new PostAdapter();
        for (int i=0;i<10;i++)
            adapter.addItem(new Post("제목입니다","작성자입니다","작성일자"));
        recyclerView.setAdapter(adapter);
    }

    public void goToBoardAnonymous(View view){
        Intent intent = new Intent(Board.this, BoardAnonymous.class);
        startActivity(intent);
        System.out.println("익명게시판으로 이동");
    }

    public void goToBoardWrite(View view){
        Intent intent = new Intent(Board.this, BoardWrite.class);
        startActivity(intent);
        System.out.println("자유게시판 게시글 작성");
    }

    public void onFilterClick(final View view) {
            final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
            getMenuInflater().inflate(R.menu.board_popup_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.action_menu1){
                        Toast.makeText(Board.this, "메뉴 1 클릭", Toast.LENGTH_SHORT).show();
                    }else if (menuItem.getItemId() == R.id.action_menu2){
                        Toast.makeText(Board.this, "메뉴 2 클릭", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Board.this, "메뉴 3 클릭", Toast.LENGTH_SHORT).show();
                    }

                    return false;
                }
            });
            popupMenu.show();
        }
}
