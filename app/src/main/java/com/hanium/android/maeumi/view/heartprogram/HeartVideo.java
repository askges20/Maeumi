package com.hanium.android.maeumi.view.heartprogram;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.helpers.YoutubeCounter;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.view.selftest.TestResult;

import java.util.concurrent.TimeUnit;

public class HeartVideo extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youtubePlayer;
    SeekBar seekBar;
    Button btn; //영상 재생(일시정지) 버튼

    private static String API_KEY;
    private static String videoId;
    private static String title;
    private static String description;

    static int heartNum;

    YoutubeCounter timer = new YoutubeCounter(this);
    boolean isCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_video);

        Intent intent = getIntent();
        API_KEY = intent.getStringExtra("API_KEY");
        videoId = intent.getStringExtra("videoId");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");

        initPlayer();   //플레이어 초기화

        TextView titleText = findViewById(R.id.youtubeViewTitleText);
        titleText.setText(title);
        TextView descriptionText = findViewById(R.id.youtubeViewDescText);
        descriptionText.setText(description);

        btn = findViewById(R.id.videoPlayBtn);
        btn.setOnClickListener(new View.OnClickListener() { //버튼 클릭 이벤트
            @Override
            public void onClick(View v) {
                playOrPause();  //영상 재생 (또는 일시정지)
                Toast.makeText(HeartVideo.this, "마음채우기 +1", Toast.LENGTH_LONG).show();
            }
        });
    }

    //영상 재생 또는 일시정지
    private void playOrPause() {
        if (youtubePlayer != null) {
            if (youtubePlayer.isPlaying()) { //재생 중이면
                youtubePlayer.pause();  //일시정지
            } else {    //일시정지 상태면
                youtubePlayer.play();   //재생
            }
        }
    }

    //플레이어 초기화
    private void initPlayer() {
        youTubePlayerView = findViewById(R.id.youtubeView);
        youTubePlayerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                youtubePlayer = player;
                youtubePlayer.cueVideo(videoId);    //썸네일 이미지 로드하는 메소드

                player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {   //play() 또는 사용자 작업으로 재생이 시작될 때
                        System.out.println("다시 재생");
                        timer.finishPause();    //다시 시간 카운트
                    }

                    @Override
                    public void onPaused() {    //pause() 또는 사용자 작업으로 재생이 일시중지될 때
                        System.out.println("일시정지");
                        timer.pause();  //스레드 일시중지 (시간 카운트 멈춤)
                    }

                    @Override
                    public void onStopped() {   //일시중지 외의 이유로 재생이 중지될 때
                        System.out.println("영상 정지됨");
                        //timer.pause();
                    }

                    @Override
                    public void onBuffering(boolean isBuffering) {    //버퍼링이 시작되거나 종료될 때
                        System.out.println("버퍼링");
                    }

                    @Override
                    public void onSeekTo(int i) {   //사용자가 취소하거나 찾기 메소드를 호출하여 재생 위치에서 이동할 때 호출됩니다.
                        System.out.println("시간 건너뛰기");
                    }
                });

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                        System.out.println("영상 로딩 중");
                    }

                    @Override
                    public void onLoaded(String s) {
                        System.out.println("영상 로딩 완료");
                        int videoTimemillis = youtubePlayer.getDurationMillis();
                        long videoTimeSec = TimeUnit.MILLISECONDS.toSeconds(videoTimemillis);
                        System.out.println("영상 길이 : " + videoTimeSec + "초");

                        setSeekbar(videoTimeSec);
                    }

                    @Override
                    public void onAdStarted() {
                        System.out.println("광고 시작");
                    }

                    @Override
                    public void onVideoStarted() {
                        System.out.println("영상 시작");
                        if(timer.getState() == Thread.State.TERMINATED) { //이미 스레드가 종료된 상태(시청을 완료한 상태)
                            System.out.println("이미 시청을 완료한 영상, 스레드 시작하지 않음");
                            return;
                        }
                        timer.start();    //스레드 생성, 시작
                    }

                    @Override
                    public void onVideoEnded() {
                        System.out.println("영상 끝");
                        timer.pause();
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                        System.out.println("onError: " + errorReason);
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    //영상 시청 시간 seekbar 세팅
    public void setSeekbar(long videoTime) {
        seekBar = findViewById(R.id.videoSeekBar);
        seekBar.setMax((int) videoTime); //최댓값 : 영상 길이
        seekBar.setEnabled(false);
    }

    //seekbar 누적 시청 시간 표시 업데이트
    public void updateSeekbar(int time) {
        if (time >= seekBar.getMax() - 1) {  //영상 시청 완료시
            seekBar.setProgress(seekBar.getMax());  //최대로 표시
            timer.finish(); //타이머 정지
            isCompleted = true;

            updateFBVideo();
            return;
        }

        //영상 시청 완료되지 않았을 때
        seekBar.setProgress(time);  //누적 시청 시간 증가
    }

    //파이어베이스에 영상 시청 기록 저장하기
    public void updateFBVideo() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference videoRef = firebaseDatabase.getReference("/마음채우기/"+ LoginUser.getInstance().getUid()+"/"+videoId);
        videoRef.setValue("watched");

        DatabaseReference heartRef = firebaseDatabase.getReference("/Users/"+ LoginUser.getInstance().getUid()+"/heart");
        heartRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){
            @Override
            public void onComplete(Task<DataSnapshot> task){
                if(task.isSuccessful()){
                    String value = task.getResult().getValue(String.class);
                    heartNum = Integer.parseInt(value);
                    heartNum = heartNum + 5;
                    heartRef.setValue(Integer.toString(heartNum));
                }
            }
        });


    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시

        if (isCompleted) {   //영상 시청을 완료했을 때
            showWatchedPopup();
        } else {
            showUnwatchedPopup();
        }
    }

    //영상 시청 완료 팝업
    public void showWatchedPopup(){
        int layout = R.layout.video_watched_popup;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        dialogBuilder.setCancelable(false); //뒤로가기 버튼 비활성화
        dialogBuilder.setView(layoutView);

        //확인 버튼
        TextView watchedBtn = layoutView.findViewById(R.id.videoWatchedBtn);
        watchedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   //현재 액티비티 종료 (뒤로가기)
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //모서리 둥글게
        dialog.show();

        //팝업 사이즈
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int x = (int) (size.x * 0.7f);
        int y = (int) (size.y * 0.4f);

        dialog.getWindow().setLayout(x, y);
    }

    //영상 시청 완료X 팝업
    public void showUnwatchedPopup(){
        int layout = R.layout.video_unwatched_popup;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        dialogBuilder.setView(layoutView);

        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //모서리 둥글게
        dialog.show();

        //취소 버튼
        TextView cancelBtn = layoutView.findViewById(R.id.videoKeepWatchBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();   //팝업창 닫기
            }
        });

        //종료하기 버튼
        TextView unwatchedBtn = layoutView.findViewById(R.id.videoUnwatchedBtn);
        unwatchedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   //현재 액티비티 종료 (뒤로가기)
            }
        });

        //팝업 사이즈
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int x = (int) (size.x * 0.7f);
        int y = (int) (size.y * 0.5f);

        dialog.getWindow().setLayout(x, y);
    }

    @Override
    public void onBackPressed() {
        goToBack(null);
    }
}
