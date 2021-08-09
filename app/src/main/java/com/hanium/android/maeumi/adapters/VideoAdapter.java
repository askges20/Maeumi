package com.hanium.android.maeumi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Video;
import com.hanium.android.maeumi.view.heartprogram.HeartProgram;

import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {

    private static String API_KEY = "AIzaSyAd7jxBYoyffM5fWPST32ZYddSlbAtix48";

    HeartProgram heartProgram;
    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<Video> items = new ArrayList<Video>();

    public VideoAdapter(HeartProgram heartProgram, Context context) {
        this.heartProgram = heartProgram;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Video getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<Video> getVideos() { return items; }

    public void setItems(ArrayList<Video> items){
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.video_item, null);

        Video video = getItem(position);
        String videoId = video.getId();

        YouTubeThumbnailView thumbnail = view.findViewById(R.id.videoThumbnailView);    //유튜브 썸네일 뷰
        thumbnail.initialize(API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(videoId);   //썸네일 불러오기
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        youTubeThumbnailLoader.release();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(heartProgram, "영상 썸네일을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView videoTitleText = view.findViewById(R.id.videoTitleText);
        videoTitleText.setText(video.getTitle());   //제목
        TextView videoDescriptionText = view.findViewById(R.id.videoDescriptionText);
        videoDescriptionText.setText(video.getDescription());   //설명

        //시청 완료한 영상만 완료 표시 나타내기
        if(!video.getIsWatched()){
            LinearLayout isWatchedView = view.findViewById(R.id.isWatchedView);
            isWatchedView.setVisibility(View.GONE);
        }

        return view;
    }
}
