package com.hanium.android.maeumi.helpers;

import android.content.Context;

import com.hanium.android.maeumi.view.heartprogram.HeartVideo;

public class YoutubeCounter extends Thread{
    HeartVideo heartVideo;

    private Thread timer;
    int watchedTime = 0;

    boolean isPaused = false;
    boolean isStop = false;

    public YoutubeCounter(HeartVideo heartVideo){
        this.heartVideo = heartVideo;
        timer = this;
    }

    @Override
    public void run(){
        while(!isStop){ //flag를 이용해서 스레드 정지
            try{
                Thread.sleep(1000); //1초간 일시정지
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!isPaused){  //일시정지 상태가 아니면
                watchedTime++;  //시청 시간 1초 증가
                System.out.println("누적 시청 시간 : " + watchedTime);
                heartVideo.updateSeekbar(watchedTime);  //seekbar 업데이트
            }
        }
        System.out.println("스레드 정지");
    }

    public void pause(){
        isPaused = true;
        System.out.println("스레드 일시정지");
    }

    public void finishPause(){
        isPaused = false;
        System.out.println("스레드 일시정지 해제");
    }

    //정지시키기
    public void finish(){
        isStop = true;
        System.out.println("스레드 정지");
    }
}
