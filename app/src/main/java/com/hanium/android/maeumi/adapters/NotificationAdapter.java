package com.hanium.android.maeumi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.MainActivity;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Notification;
import com.hanium.android.maeumi.model.Post;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.board.PostContent;
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.view.profile.MyNotifications;

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
        DatabaseReference notifyRef = firebaseDatabase.getReference(pathStr);
        notifyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();  //기존 ArrayList 비우기
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

        //알림 이미지
        ImageView notifyImg = view.findViewById(R.id.notificationImg);
        if (title.contains("회원가입")){
            notifyImg.setImageResource(R.drawable.maeumi_happy);
        } else if (title.contains("댓글")){
            notifyImg.setImageResource(R.drawable.comment_icon);
        } else {
            notifyImg.setImageResource(R.drawable.heart_icon_2);
        }

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
                    //이용 안내 페이지 만들어지면 추가할 부분
                    return;
                }

                //댓글 알림 -> 게시글로 이동
                if (title.contains("댓글")){
                    notify.setRead(true, "comment");   //읽음 여부 수정
                    findPostFromDB(mContext, notify.getBoardType(), notify.getPostNum());
                    return;
                }

                //마음온도 60점 달성 -> 게시판 이동
                if (title.contains("60점")){
                    notify.setRead(true, "board");   //읽음 여부 수정
                    Intent intent = new Intent(mContext, Board.class);
                    mContext.startActivity(intent);
                    return;
                }
            }
        });


        return view;
    }

    // 댓글 알림과 관련된 게시글 DB에서 찾기
    public void findPostFromDB(Context mContext, String boardType, String postNum) {
        String boardName = "";
        switch(boardType){
            case "free":
                boardName = "자유게시판";
                break;
            case "question":
                boardName = "질문게시판";
                break;
            case "tip":
                boardName = "꿀팁게시판";
                break;
            case "anonymous":
                boardName = "익명게시판";
                break;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference("/게시판/"+boardName+"/");
        System.out.println(postRef.orderByChild(postNum));

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean find = false;
                for (DataSnapshot dateSnap : dataSnapshot.getChildren()) { //하위 구조 (작성일자)
                    for (DataSnapshot snap : dateSnap.getChildren()) { //하위 구조 (게시글)
                        if (snap.getKey().equals(postNum)){ //게시글 번호와 일치하는 글
                            PostAdapter.curPost = snap.getValue(Post.class);
                            Intent intent = new Intent(mContext, PostContent.class);
                            intent.putExtra("boardType", boardType);
                            mContext.startActivity(intent); //게시글 내용 페이지로 이동
                            find = true;
                        }
                    }
                }
                //DB에 게시글이 존재하지 않으면 (삭제된 게시글이면)
                if (!find){
                    Toast.makeText(mContext, "삭제된 게시글입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }
}
