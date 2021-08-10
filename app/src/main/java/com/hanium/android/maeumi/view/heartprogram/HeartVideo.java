package com.hanium.android.maeumi.view.heartprogram;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
        heartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                heartNum = Integer.parseInt(snapshot.getValue(String.class));

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        heartNum +=5;
        String heartPlus = Integer.toString(heartNum);
        heartRef.setValue(heartPlus);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        AlertDialog dialog;

        if (isCompleted) {   //영상 시청을 완료했을 때
            dialog = new AlertDialog.Builder(this)
                    .setMessage("영상 시청 완료!!")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            youtubePlayer.release();    //YoutubePlayer에서 사용한 리소스 해제
                            timer.finish();
                            finish();   //현재 액티비티 없애기
                        }
                    })
                    .show();
        } else {
            dialog = new AlertDialog.Builder(this)
                    .setMessage("아직 영상 시청을 완료하지 않았어요. 영상 시청을 종료할까요? (완료하지 않으면 영상 시청 기록이 남지 않아요.)")
                    .setPositiveButton("종료하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            youtubePlayer.release();    //YoutubePlayer에서 사용한 리소스 해제
                            timer.finish();
                            finish();   //현재 액티비티 없애기
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        TextView dialogMessage = (TextView) dialog.findViewById(android.R.id.message);
        dialogMessage.setTextSize(18);

        //왜 여기만 커스텀 폰트 적용이 안되는지 모르겠음
    }

    @Override
    public void onBackPressed() {
        goToBack(null);
    }
}
