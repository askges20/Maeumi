package com.hanium.android.maeumi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;
import com.hanium.android.maeumi.view.diary.DiaryMain;
import com.hanium.android.maeumi.view.heartprogram.HeartProgram;
import com.hanium.android.maeumi.view.loading.LoginActivity;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.view.profile.Profile;
import com.hanium.android.maeumi.view.selftest.SelfTest;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    ImageView mainDrawerBtn;
    NavigationView navigationView;
    Button logoutBtn;
    TextView randomText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_include_drawer);

        /*
        //네트워크 연결 상태 확인
        if(!isConnect2Network()){   //연결되어 있지 않으면
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("네트워크 연결상태 확인 후 재접속해주세요");
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    finish();
                }
            });
            dialog.setCancelable(false);    //뒤로가기 키, 배경 터치 불가
            dialog.show();
        }
         */
        progressBar = findViewById(R.id.progressBar);

        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        navigationView = findViewById(R.id.main_navigation_view);
        //사이드바(NavigationView) 메뉴 클릭 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();

                int id = item.getItemId();

                if (id==R.id.drawer_menu_test){
                    startActivity(new Intent(MainActivity.this, SelfTest.class));
                } else if (id==R.id.drawer_menu_chat){
                    startActivity(new Intent(MainActivity.this, ChatBot.class));
                } else if (id==R.id.drawer_menu_diary){
                    startActivity(new Intent(MainActivity.this, DiaryMain.class));
                } else if (id==R.id.drawer_menu_board){
                    startActivity(new Intent(MainActivity.this, Board.class));
                } else if (id==R.id.drawer_menu_mypage){
                    startActivity(new Intent(MainActivity.this, Profile.class));
                }else if (id==R.id.drawer_menu_youtube){
                    startActivity(new Intent(MainActivity.this, HeartProgram.class));
                }

                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        TextView userAlias = header.findViewById(R.id.mainUserAliasText);
        TextView userSchool = header.findViewById(R.id.mainUserSchoolText);

        logoutBtn = header.findViewById(R.id.mainLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutBtnEvent();  //로그아웃 버튼 이벤트
            }
        });

        mainDrawerBtn = findViewById(R.id.mainDrawerBtn);
        mainDrawerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        randomText = findViewById(R.id.mainRandomText);
        setRandomText();

        askForTest(); //테스트 진행 여부에 따른 팝업
        getHeart(); // 마음채우기
    }

    public void goToDiary(View view) {
        Intent intent = new Intent(MainActivity.this, DiaryMain.class);
        startActivity(intent);
    }

    public void goToSelfTest(View view) {
        Intent intent = new Intent(MainActivity.this, SelfTest.class);
        startActivity(intent);
    }

    public void goToChatBot(View view) {
        Intent intent = new Intent(MainActivity.this, ChatBot.class);
        startActivity(intent);
    }

    public void goToBoard(View view) {
        Intent intent = new Intent(MainActivity.this, Board.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }

    //랜덤 문구 출력
    public void setRandomText() {
        String []arr = getResources().getStringArray(R.array.random_text);
        int randNum = (int) (Math.random() * 20);
        randomText.setText(arr[randNum]);
    }

    @Override
    public void onBackPressed() {   //뒤로가기 버튼 클릭 시
        //사이드바가 열려있으면 닫기
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {    //사이드바가 닫혀있으면 앱 종료 팝업
            showEndDialog();
        }
    }

    //앱을 종료할지 묻는 팝업 띄우기
    public void showEndDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("앱을 종료하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        dialog.show();
    }

    //로그아웃 버튼 클릭 이벤트
    public void signOutBtnEvent() {
        new AlertDialog.Builder(this)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //네 클릭
                        processSignOut();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //취소 클릭
                    }
                })
                .show();
    }

    //로그아웃 진행
    public void processSignOut() {
        LoginUser.signOutUser();    //싱글톤 객체 new
        FirebaseAuth.getInstance().signOut();   //FirebaseAuth에서 로그아웃

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);  //로그인 화면으로 이동
    }

    //네트워크 연결 여부 리턴
    public boolean isConnect2Network() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo.State networkState = manager.getActiveNetworkInfo().getState();
        if (networkState == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return true;
    }

    //테스트 진행 팝업
    public boolean askForTest() {
        //테스트 결과가 존재하면
        if (LoginUser.getInstance().getVictimScore() != null) {
            return false;
        }

        //테스트 결과가 존재하지 않으면
        new AlertDialog.Builder(this)
                .setMessage("아직 진단테스트를 이용하지 않으셨습니다. 진단테스트를 진행하시겠습니까?") //진단테스트 권유.. 메세지? 내용 수정할수도
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //네 클릭
                        startActivity(new Intent(MainActivity.this, SelfTest.class));
                    }
                })
                .setNegativeButton("다음에 할게요", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //취소 클릭
                    }
                })
                .show();

        return true;
    }

    //마음 채우기
    public void getHeart(){
        if(LoginUser.getInstance().getHeart() != null){
            progressBar.setProgress(Integer.parseInt(LoginUser.getInstance().getHeart()));
        }else{
            progressBar.setProgress(5);
        }
    }
}