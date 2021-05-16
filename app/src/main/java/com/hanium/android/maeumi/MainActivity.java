package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Main Main");

        //firebase 연동 테스트
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                System.out.println("Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                System.out.println("Failed to read value.");
            }
        });
    }

    public void goToDiary(View view){
        Intent intent = new Intent(MainActivity.this, Diary.class);
        startActivity(intent);
        System.out.println("Move To Diary");
    }

    public void goToSelfTest(View view){
        Intent intent = new Intent(MainActivity.this, SelfTest.class);
        startActivity(intent);
        System.out.println("Move To SelfTest");
    }

    public void goToChatBot(View view){
        Intent intent = new Intent(MainActivity.this, ChatBot.class);
        startActivity(intent);
        System.out.println("Move To ChatBot");
    }

    public void goToBoard(View view){
        Intent intent = new Intent(MainActivity.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
    }

    public void goToProfile(View view){
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
        System.out.println("Move To Profile");
    }

}