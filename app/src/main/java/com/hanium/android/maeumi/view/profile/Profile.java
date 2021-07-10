package com.hanium.android.maeumi.view.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;

public class Profile extends Activity {

    FirebaseDatabase database;
    DatabaseReference userRef;

    String loginUserUid;
    TextView loginUserNameText;
    TextView loginUserEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        loginUserNameText = findViewById(R.id.loginUserNameText);
        loginUserEmailText = findViewById(R.id.loginUserEmailText);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();    //현재 로그인한 사용자
        loginUserUid = user.getUid();   //사용자 Uid

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("/Users/" + loginUserUid + "/");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                loginUserNameText.setText("이름 : " + snapshot.child("name").getValue(String.class)); //사용자 이름 표시
                loginUserEmailText.setText("이메일 : " + snapshot.child("email").getValue(String.class));  //사용자 이메일 표시
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
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
