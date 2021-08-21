package com.hanium.android.maeumi.model;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.PostAdapter;
import com.hanium.android.maeumi.view.loading.LoginUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post {
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageRef;
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

    public void setLikeUsers(boolean fromNotify, TextView likeBtnText, ImageView likeHeartImg) {
        postNum = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19) + writerUid;

        database = FirebaseDatabase.getInstance();
        DatabaseReference likeRef = database.getReference("/공감/" + postNum + "/");
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likeUsers.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    likeUsers.add(snap.getKey());   //리스트에 uid 추가
                }
                if(!fromNotify) {
                    postAdapter.notifyDataSetChanged(); //어댑터에 알림
                } else {
                    likeBtnText.setText(""+likeUsers.size());
                    if (likeUsers.contains(LoginUser.getInstance().getUid())){
                        likeHeartImg.setImageResource(R.drawable.heart_icon_2);
                    }
                }
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
    public void addLikeUser(String uid, boolean fromNotify) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        likeUsers.add(uid);
        DatabaseReference likeRef = database.getReference("/공감/" + postNum + "/" + uid);
        likeRef.setValue("like");   //DB 추가

        if(!fromNotify){
            postAdapter.notifyDataSetChanged(); //어댑터에 변경 알림
        }
    }

    //공감 취소
    public void removeLikeUser(String uid, boolean fromNotify) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        likeUsers.remove(uid);
        DatabaseReference likeRef = database.getReference("/공감/" + postNum + "/" + uid);
        likeRef.setValue(null); //삭제

        if(!fromNotify){
            postAdapter.notifyDataSetChanged(); //어댑터에 변경 알림
        }
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

    // 사진 DB저장
    public void saveImg(Bitmap imgBitmap,String ref,String path){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("/board");

        StorageReference imgSaveRef = storageRef.child(ref).child(path);

        Bitmap bitmap = imgBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgSaveRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("error - " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("성공");
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
