package com.hanium.android.maeumi.view.loading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password, name, alias, school;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference aliasRef;
    private String userEmail, userPassword, userName, userGender, userAlias, userSchool;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        name = findViewById(R.id.userName);
        alias = findViewById(R.id.alias);
        school = findViewById(R.id.school);
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.girl) {
                    userGender = "???";
                } else if (checkedId == R.id.man) {
                    userGender = "???";
                }
            }
        });

    }

    public void onSignUpClick(View view) {

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        userName = name.getText().toString();
        userAlias = alias.getText().toString();
        userSchool = school.getText().toString();

        Pattern pattern = Patterns.EMAIL_ADDRESS;

        if (!pattern.matcher(userEmail).matches()) {
            Toast.makeText(SignUpActivity.this, "????????? ????????? ???????????????", Toast.LENGTH_LONG).show();
        } else if (!Pattern.matches("^(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", userPassword)) {
            Toast.makeText(SignUpActivity.this, "8??? ??????, ??????, ??????????????? ?????? 1????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else if (userName.equals("")) {
            Toast.makeText(SignUpActivity.this, "????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else if (userGender == null) {
            Toast.makeText(SignUpActivity.this, "????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else if (userAlias == null) {
            Toast.makeText(SignUpActivity.this, "???????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else if (userSchool == null) {
            Toast.makeText(SignUpActivity.this, "????????? ??????????????????.", Toast.LENGTH_LONG).show();
        } else {
            checkAlias(userAlias);
        }
    }
        private void createUser(String email, String password, String userName, String UserGender, String userAlias, String userSchool) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // ???????????? ?????????
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            // ?????? ?????? ?????????
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "???????????? ??????, ?????? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(SignUpActivity.this, "?????? ????????? ??????", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = userName;
                            String gender = UserGender;
                            String alias = userAlias;
                            String school = userSchool;
                            String heart = "-1";

                            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                            final Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE,-1);
                            String notifyDate = sdFormat.format(cal.getTime());

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("name", name);
                            hashMap.put("gender", gender);
                            hashMap.put("alias", alias);
                            hashMap.put("school", school);
                            hashMap.put("heart", heart);
                            hashMap.put("notifyDate", notifyDate);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);

                            addNotifyToDB();

                        } else {
                            // ????????? ????????? ??????
                            Toast.makeText(SignUpActivity.this, "????????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void checkAlias(String name){

        database = FirebaseDatabase.getInstance();
        aliasRef = database.getReference("/Users/");

        aliasRef.orderByChild("alias").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(SignUpActivity.this, "????????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
                }else{
                    createUser(userEmail, userPassword, userName, userGender, userAlias, userSchool);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    //DB??? ???????????? ?????? ??????
    public void addNotifyToDB() {
        Date time = Calendar.getInstance().getTime();;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String addTime = format1.format(time);

        DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String notifyNum = format2.format(time) + "comment";

        DatabaseReference notifyRef = database.getReference("/??????/"+ LoginUser.getInstance().getUid()+"/");

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> notifyValues = null;

        Notification notify = new Notification("new", "???????????? ?????? ?????? ???????????????!", "????????? ?????? ????????? ?????????????????????????", null, null, addTime, false);
        notifyValues = notify.toMap();
        childUpdates.put(notifyNum, notifyValues);
        notifyRef.updateChildren(childUpdates);
    }

    public void onLoginClick(View view){
        finish();
    }

}