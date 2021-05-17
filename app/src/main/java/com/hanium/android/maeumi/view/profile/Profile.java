package com.hanium.android.maeumi.view.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.diary.DiaryContent;

public class Profile extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
    }

    public void goToProfileEdit(View view){
        Intent intent = new Intent(Profile.this,ProfileEdit.class);
        startActivity(intent);
        System.out.println("Move To Profile Edit");
    }

    public void deleteConfirm(View view){
        new AlertDialog.Builder(this)
                .setTitle("게시글 삭제")
                .setMessage("게시글을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Profile.this, "삭제완료", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Profile.this, "취소완료", Toast.LENGTH_SHORT).show();
                    }})
                .show();
    }
}
