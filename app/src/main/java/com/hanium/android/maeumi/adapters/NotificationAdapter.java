package com.hanium.android.maeumi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.MainActivity;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Notification;
import com.hanium.android.maeumi.model.Video;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.heartprogram.HeartProgram;
import com.hanium.android.maeumi.view.heartprogram.HeartVideo;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.view.profile.MyNotifications;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String pathStr = "/알림/"+ LoginUser.getInstance().getUid()+"/";

    MyNotifications myNotifications;
    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<Notification> items = new ArrayList<Notification>();    //알림들

    public NotificationAdapter(MyNotifications myNotifications, Context context) {
        this.myNotifications = myNotifications;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        readNotifyFromFB();    //DB 조회해서 알림 가져오기
    }

    //DB에서 알림 읽어오기
    public void readNotifyFromFB(){
        DatabaseReference videoRef = firebaseDatabase.getReference(pathStr);
        videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    items.add(0, snap.getValue(Notification.class));
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Notification getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<Notification> getNotifications() { return items; }

    public void setItems(ArrayList<Notification> items){
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.notification_item, null);

        Notification notify = getItem(position);
        String title = notify.getTitle();
        String content = notify.getContent();
        String dateTime = notify.getDateTime();

        //알림 정보 화면에 표시
        TextView notifyTitleText = view.findViewById(R.id.notifyTitleText);
        notifyTitleText.setText(title);
        TextView notifyContentText = view.findViewById(R.id.notifyContentText);
        notifyContentText.setText(content);
        TextView notifyDateTimeText = view.findViewById(R.id.notifyDateTimeText);
        notifyDateTimeText.setText(dateTime);

        //각 아이템 클릭 시 영상 시청 화면으로 이동
        LinearLayout notificationItemView = view.findViewById(R.id.notificationItemView);
        notificationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 축하 알림 -> 이용안내
                if (title.contains("회원가입")){
                    Intent intent = new Intent(mContext, MainActivity.class);
                    return;
                }

                //댓글 알림 -> 게시글로 이동
                if (title.contains("댓글")){
                    return;
                }

                //마음온도 60점 달성 -> 게시판 이동
                if (title.contains("60점")){
                    Intent intent = new Intent(mContext, Board.class);
                    mContext.startActivity(intent);
                    return;
                }
            }
        });


        return view;
    }
}
