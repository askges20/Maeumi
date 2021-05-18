package com.hanium.android.maeumi.view.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.android.maeumi.R;

public class Board extends AppCompatActivity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);
    }

    public void goToBoardAnonymous(View view){
        Intent intent = new Intent(Board.this, BoardAnonymous.class);
        startActivity(intent);
        System.out.println("Move To BoardAnonymous");
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
