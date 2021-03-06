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

    DatabaseReference notifyDateRef;
    FirebaseDatabase database;

    NotificationAdapter notifyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_include_drawer);

        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_navigation_view);
        //????????????(NavigationView) ?????? ?????? ?????????
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

        //???????????? ??????
        View header = navigationView.getHeaderView(0);
        TextView userAlias = header.findViewById(R.id.mainUserAliasText);
        TextView userSchool = header.findViewById(R.id.mainUserSchoolText);

        String alias = LoginUser.getInstance().getAlias();

        //????????? ???????????? ???????????? DB ???????????? ???????????? ?????? null?????? ?????? ????????? ??????
        //??? ?????? ????????? ????????? ???????????? ??????
        if (alias == null) {
            userAlias.setText("???????????? ?????? ?????? ???????????????!");
            userSchool.setVisibility(View.GONE);
        } else {    //????????? ????????? ???????????????
            userAlias.setText(alias + " ???");  //?????? ????????? ??????
            userSchool.setText(LoginUser.getInstance().getSchool());    //?????? ?????? ??????
        }

        //???????????? ??????
        logoutBtn = header.findViewById(R.id.mainLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutBtnEvent();  //???????????? ?????? ?????????
            }
        });

        //???????????? ?????? ??????
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

        //?????? ??????
        randomText = findViewById(R.id.mainRandomText);
        setRandomText();

        showHelpPopUp(); //???????????? ??????

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
        if (heart == -1) {  //?????????????????? ???????????? ?????? ??????
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.board_before_test)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            goToSelfTest(null);
                        }
                    })
                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
            dialogMessage.setTextSize(16);
        } else if (heart < 60) {   //?????? ????????? 60 ????????? ?????? ????????? ?????? ??????
            showBoardBeforeHeartPopup(R.layout.board_before_heart_popup);    //?????? ????????? ?????? ??????
        } else {
            Intent intent = new Intent(MainActivity.this, Board.class);
            startActivity(intent);
        }
    }

    public void prohibitBeforeTestMessage(View view) {
        int heart = Integer.parseInt(LoginUser.getInstance().getHeart());
        if (heart == -1) {  //?????????????????? ???????????? ?????? ??????
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.heartProgram_before_test)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            goToSelfTest(null);
                        }
                    })
                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //????????? ?????????
        dialog.show();

        //???????????? ??????
        TextView moveBtn = layoutView.findViewById(R.id.moveToHeartBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeartProgram.class);
                startActivity(intent);  //?????? ???????????? ??????
                dialog.hide();  //?????? ?????????
            }
        });

        //?????? ?????????
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

    //?????? ?????? ??????
    public void setRandomText() {
        String[] arr = getResources().getStringArray(R.array.random_text);
        int randNum = (int) (Math.random() * arr.length);
        randomText.setText(arr[randNum]);
    }

    @Override
    public void onBackPressed() {   //???????????? ?????? ?????? ???
        //??????????????? ??????????????? ??????
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {    //??????????????? ??????????????? ??? ?????? ??????
            showEndDialog();
        }
    }

    //?????? ???????????? ?????? ?????? ?????????
    public void showEndDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("?????? ?????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finish();   //?????? ???????????? ?????????
                    }
                })
                .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                }).show();

        //?????? ?????? ??????
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    //???????????? ?????? ?????? ?????????
    public void signOutBtnEvent() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("???????????? ???????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //??? ??????
                        processSignOut();
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //?????? ??????
                    }
                })
                .show();

        //?????? ?????? ??????
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    //???????????? ??????
    public void processSignOut() {
        LoginUser.signOutUser();    //????????? ?????? new
        FirebaseAuth.getInstance().signOut();   //FirebaseAuth?????? ????????????

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);  //????????? ???????????? ??????
    }

    //????????? ?????? ??????
    public void showHelpPopUp() {
        String dbDate = LoginUser.getInstance().getNotifyDate();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        String strCurDate = sdFormat.format(nowDate);

        AlertDialog.Builder helpPopup = new AlertDialog.Builder(MainActivity.this);
        helpPopup.setTitle("????????????");
        helpPopup.setIcon(R.drawable.maeumi_main_img);
        helpPopup.setMessage("??????????????? ?????????????????? ??????????????? ???????????????");
        helpPopup.setCancelable(false);

        helpPopup.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
        helpPopup.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, Guide.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        helpPopup.setNeutralButton("?????? ?????? ??????", new DialogInterface.OnClickListener() {
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
        //????????? ????????? ????????????
        if (LoginUser.getInstance().getVictimScore() != null) {
            return false;
        }
        //????????? ????????? ???????????? ?????????
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("?????? ?????????????????? ???????????? ??????????????????. ?????????????????? ?????????????????????????") //??????????????? ??????.. ?????????? ?????? ???????????????
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //??? ??????
                        startActivity(new Intent(MainActivity.this, SelfTest.class));
                    }
                })
                .setNegativeButton("????????? ?????????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //?????? ??????
                    }
                })
                .show();

        //?????? ?????? ??????
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);

        return true;
    }

}