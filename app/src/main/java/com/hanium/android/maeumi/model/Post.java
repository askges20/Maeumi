package com.hanium.android.maeumi.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.adapters.PostAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post {
    FirebaseDatabase database;
    PostAdapter postAdapter;
    String postNum;

    private String title;       //제목
    private String content;     //내용
    private String writer;      //작성자
    private String writeDate;   //작성일자
    private String writerUid;    //작성자 uid

    private ArrayList<String> likeUsers = new ArrayList<>(); //공감(좋아요)을 누른 사용자 uid
    private int commentCnt; //댓글 개수

    public Post() {

    }

    public Post(String title, String content, String writer, String writeDate, String writerUid) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writeDate = writeDate;
        this.writerUid = writerUid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("writer", writer);
        result.put("writeDate", writeDate);
        result.put("writerUid", writerUid);
        return result;
    }

    public void setAdapter(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
    }


    /* 공감 관련 메소드*/

    public void setLikeUsers() {
        postNum = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19) + writerUid;

        database = FirebaseDatabase.getInstance();
        DatabaseReference likeRef = database.getReference("/공감/" + postNum + "/");
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    likeUsers.add(snap.getKey());   //리스트에 uid 추가
                    System.out.println("언제 추가되는데 대체");
                }
                postAdapter.notifyDataSetChanged(); //어댑터에 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    public ArrayList<String> getLikeUsers() {
        return likeUsers;
    }

    //공감하기
    public void addLikeUser(String uid) {
        likeUsers.add(uid);
        DatabaseReference likeRef = database.getReference("/공감/" + postNum + "/" + uid);
        likeRef.setValue("like");
        postAdapter.notifyDataSetChanged(); //어댑터에 변경 알림
    }

    //공감 취소
    public void removeLikeUser(String uid) {
        likeUsers.remove(uid);
        DatabaseReference likeRef = database.getReference("/공감/" + postNum + "/" + uid);
        likeRef.setValue(null);
        postAdapter.notifyDataSetChanged(); //어댑터에 변경 알림
    }


    /* 댓글 개수 관련 메소드 */

    public void setCommentCnt() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference commentRef = database.getReference("/댓글/" + postNum + "/");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    cnt++;
                }
                commentCnt = cnt;   //댓글 개수 저장
                postAdapter.notifyDataSetChanged(); //어댑터에 변경 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getCommentCnt() {
        return commentCnt;
    }


    /* Getter/Setter */

    public int getLikeUsersCnt() {
        return likeUsers.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getWriterUid() {
        return writerUid;
    }

    public void setWriterUid(String writerUid) {
        this.writerUid = writerUid;
    }
}
