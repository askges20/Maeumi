package com.hanium.android.maeumi.view.heartprogram;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hanium.android.maeumi.R;

public class HeartProgram extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    Button btn;
    YouTubePlayer.OnInitializedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_program);

        btn = findViewById(R.id.videoPlayBtn);
        youTubePlayerView = findViewById(R.id.youtubeView);
        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("CbGpbuOhtVE"); //동영상
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyAd7jxBYoyffM5fWPST32ZYddSlbAtix48",listener); //api 키
            }
        });
        
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}