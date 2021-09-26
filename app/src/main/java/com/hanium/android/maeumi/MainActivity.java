package com.hanium.android.maeumi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanium.android.maeumi.adapters.NotificationAdapter;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;
import com.hanium.android.maeumi.view.diary.DiaryMain;
import com.hanium.android.maeumi.view.guide.Guide;
import com.hanium.android.maeumi.view.heartprogram.HeartProgram;
import com.hanium.android.maeumi.view.loading.LoginActivity;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.view.profile.MyNotifications;
import com.hanium.android.maeumi.view.profile.Profile;
import com.hanium.android.maeumi.view.selftest.SelfTest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    ImageView mainDrawerBtn;
    ImageView goToNotificationBtn;
    NavigationView navigationView;
    Button logoutBtn;
    TextView randomText;
    ProgressBar progressBar;

    DatabaseReference heartRef;
    DatabaseReference notifyDateRef;
    FirebaseDatabase database;

    NotificationAdapter notifyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        상태바 없애기
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        navigationView = findViewById(R.id.main_navigation_view);
        //사이드바(NavigationView) 메뉴 클릭 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();

                int id = item.getItemId();

                if (id == R.id.drawer_menu_test) {
                    goToSelfTest(null);
                } else if (id == R.id.drawer_menu_chat) {
                    goToChatBot(null);
                } else if (id == R.id.drawer_menu_diary) {
                    goToDiary(null);
                } else if (id == R.id.drawer_menu_board) {
                    goToBoard(null);
                } else if (id == R.id.drawer_menu_mypage) {
                    startActivity(new Intent(MainActivity.this, Profile.class));
                } else if (id == R.id.drawer_menu_youtube) {
                    prohibitBeforeTestMessage(null);
                } else if (id == R.id.guide) {
                    startActivity(new Intent(MainActivity.this, Guide.class));
                }

                return true;
            }
        });

        //사이드바 헤더
        View header = navigationView.getHeaderView(0);
        TextView userAlias = header.findViewById(R.id.mainUserAliasText);
        TextView userSchool = header.findViewById(R.id.mainUserSchoolText);

        String alias = LoginUser.getInstance().getAlias();

        //로그인 화면에서 넘어오면 DB 읽어오는 시간으로 인해 null값이 뜨는 경우가 있음
        //이 때는 사용자 정보를 출력하지 않음
        if (alias == null) {
            userAlias.setText("마음이에 오신 것을 환영합니다!");
            userSchool.setVisibility(View.GONE);
        } else {    //사용자 정보를 읽어왔으면
            userAlias.setText(alias + " 님");  //유저 닉네임 출력
            userSchool.setText(LoginUser.getInstance().getSchool());    //유저 학교 출력
        }

        //로그아웃 버튼
        logoutBtn = header.findViewById(R.id.mainLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutBtnEvent();  //로그아웃 버튼 이벤트
            }
        });

        //사이드바 여는 버튼
        mainDrawerBtn = findViewById(R.id.mainDrawerBtn);
        mainDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        goToNotificationBtn = findViewById(R.id.goToNotification);
        goToNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyNotifications.class);
                startActivity(intent);
            }
        });

        //랜덤 문구
        randomText = findViewById(R.id.mainRandomText);
        setRandomText();

        showHelpPopUp(); //이용안내 팝업

        notifyAdapter = new NotificationAdapter(this, goToNotificationBtn);
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
        int heart = Integer.parseInt(LoginUser.getInstance().getHeart());
        if (heart == -1) {  //진단테스트를 이용하지 않은 경우
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.board_before_test)
                    .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            goToSelfTest(null);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
            dialogMessage.setTextSize(16);
        } else if (heart < 60) {   //마음 온도가 60 미만인 경우 게시판 이용 불가
            showBoardBeforeHeartPopup(R.layout.board_before_heart_popup);    //마음 채우기 안내 팝업
        } else {
            Intent intent = new Intent(MainActivity.this, Board.class);
            startActivity(intent);
        }
    }

    public void prohibitBeforeTestMessage(View view) {
        int heart = Integer.parseInt(LoginUser.getInstance().getHeart());
        if (heart == -1) {  //진단테스트를 이용하지 않은 경우
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.heartProgram_before_test)
                    .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            goToSelfTest(null);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
            dialogMessage.setTextSize(16);
        } else {
            startActivity(new Intent(MainActivity.this, HeartProgram.class));
        }
    }

    public void showBoardBeforeHeartPopup(int layout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        dialogBuilder.setView(layoutView);

        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //모서리 둥글게
        dialog.show();

        //이동하기 버튼
        TextView moveBtn = layoutView.findViewById(R.id.moveToHeartBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeartProgram.class);
                startActivity(intent);  //마음 채우기로 이동
                dialog.hide();  //팝업 없애기
            }
        });

        //팝업 사이즈
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);

    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }

    //랜덤 문구 출력
    public void setRandomText() {
        String[] arr = getResources().getStringArray(R.array.random_text);
        int randNum = (int) (Math.random() * arr.length);
        randomText.setText(arr[randNum]);
    }

    @Override
    public void onBackPressed() {   //뒤로가기 버튼 클릭 시
        //사이드바가 열려있으면 닫기
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {    //사이드바가 닫혀있으면 앱 종료 팝업
            showEndDialog();
        }
    }

    //앱을 종료할지 묻는 팝업 띄우기
    public void showEndDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finish();   //현재 액티비티 없애기
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                }).show();

        //폰트 크기 조정
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    //로그아웃 버튼 클릭 이벤트
    public void signOutBtnEvent() {
        AlertDialog dialog = new AlertDialog.Builder(this)
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

        //폰트 크기 조정
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
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
    public void showHelpPopUp() {
        String dbDate = LoginUser.getInstance().getNotifyDate();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        String strCurDate = sdFormat.format(nowDate);

        AlertDialog.Builder helpPopup = new AlertDialog.Builder(MainActivity.this);
        helpPopup.setTitle("이용안내");
        helpPopup.setIcon(R.drawable.maeumi_main_img);
        helpPopup.setMessage("처음이라면 사이드바에서 이용안내를 받아보세요");
        helpPopup.setCancelable(false);

        helpPopup.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTestPopUp();
                    }
                },300);
            }
        });
        helpPopup.setNegativeButton("이동", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, Guide.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        helpPopup.setNeutralButton("오늘 그만 보기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                database = FirebaseDatabase.getInstance();
                notifyDateRef = database.getReference("/Users/" + LoginUser.getInstance().getUid() + "/notifyDate/");
                notifyDateRef.setValue(strCurDate);
                dialog.dismiss();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTestPopUp();
                    }
                },300);
            }
        });

        if(dbDate != null){
            try{
                Date date = sdFormat.parse(dbDate);
                Date today = sdFormat.parse(strCurDate);

                if(today.after(date)){
                    helpPopup.show();
                }else{
                    showTestPopUp();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public boolean showTestPopUp() {
        //테스트 결과가 존재하면
        if (LoginUser.getInstance().getVictimScore() != null) {
            return false;
        }
        //테스트 결과가 존재하지 않으면
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
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

        //폰트 크기 조정
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);

        return true;
    }

}