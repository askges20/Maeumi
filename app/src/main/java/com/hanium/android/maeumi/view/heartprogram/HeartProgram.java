package com.hanium.android.maeumi.view.heartprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.PostAdapter;
import com.hanium.android.maeumi.adapters.QuestionAdapter;
import com.hanium.android.maeumi.adapters.VideoAdapter;
import com.hanium.android.maeumi.model.Question;
import com.hanium.android.maeumi.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HeartProgram extends YouTubeBaseActivity {

    ListView videoListView;
    private ArrayList<Video> videoList = new ArrayList<Video>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_program);

        videoListView = findViewById(R.id.heartVideoListView);

        String jsonString = getJsonString();    //json 파일 읽기
        jsonParsing(jsonString);    //json 파싱 -> ArrayList에 담기

        //어댑터를 이용해서 리스트뷰에 데이터 넘김
        VideoAdapter adapter = new VideoAdapter(this, this);
        adapter.setItems(videoList);
        videoListView.setAdapter(adapter);   //어댑터 등록
    }

    //테스트 문항 json 파일 읽어오기
    private String getJsonString() {
        String json = "";

        try {
            InputStream is = getAssets().open("videos.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray videoArray = jsonObject.getJSONArray("Videos");

            for (int i = 0; i < videoArray.length(); i++) {
                JSONObject videoObject = videoArray.getJSONObject(i);
                String id = videoObject.getString("id");
                String title = videoObject.getString("title");
                String description = videoObject.getString("description");
                Video video = new Video(id, title, description);

                videoList.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showHeartGuide(View view) {
        Intent intent = new Intent(HeartProgram.this, HeartGuide.class);
        startActivity(intent);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }
}