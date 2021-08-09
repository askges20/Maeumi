package com.hanium.android.maeumi.view.heartprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hanium.android.maeumi.R;

public class HeartVideo extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youtubePlayer;
    Button btn; //영상 재생(일시정지) 버튼

    private static String API_KEY;
    private static String videoId;
    private static String title;
    private static String description;

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
        if(youtubePlayer != null){
            if(youtubePlayer.isPlaying()) { //재생 중이면
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
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                youtubePlayer = player;
                youtubePlayer.cueVideo(videoId);    //썸네일 이미지 로드하는 메소드

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                        System.out.println("영상 로딩 중");
                    }

                    @Override
                    public void onLoaded(String s) {
                        System.out.println("영상 로딩 완료");
                    }

                    @Override
                    public void onAdStarted() {
                        System.out.println("광고 시작");
                    }

                    @Override
                    public void onVideoStarted() {
                        System.out.println("영상 시작");
                    }

                    @Override
                    public void onVideoEnded() {
                        System.out.println("영상 끝");
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

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}
