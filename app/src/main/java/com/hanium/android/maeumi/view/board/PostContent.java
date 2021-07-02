package com.hanium.android.maeumi.view.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Comment;
import com.hanium.android.maeumi.viewmodel.CommentAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostContent extends AppCompatActivity {
    public static Activity PostContent_Activity;

    FirebaseDatabase database;
    DatabaseReference postRef;
    DatabaseReference commentRef;
    CommentAdapter commentAdapter;

    String title, content, writeDate, writer, boardType;

    ListView commentList; //댓글 목록

    TextView titleText; //제목 텍스트
    TextView contentText;   //내용 텍스트
    TextView dateText;  //날짜 텍스트
    TextView writerText;    //작성자 텍스트

    EditText writtenCommentText;    //댓글 작성칸
    Button addCommentBtn;   //댓글 등록 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        PostContent_Activity = this;

        database = FirebaseDatabase.getInstance();

        titleText = findViewById(R.id.postTitleText);
        contentText = findViewById(R.id.postContentText);
        dateText = findViewById(R.id.postDateText);
        writerText = findViewById(R.id.postWriterText);

        writtenCommentText = findViewById(R.id.writtenCommentText);
        addCommentBtn = findViewById(R.id.addCommentBtn);

        Intent prevIntent = getIntent();
        title = prevIntent.getStringExtra("title");
        titleText.setText(title);
        content = prevIntent.getStringExtra("content");
        contentText.setText(content);
        writeDate = prevIntent.getStringExtra("writeDate");
        dateText.setText(writeDate);
        writer = prevIntent.getStringExtra("writer");
        writerText.setText(writer);
        boardType = prevIntent.getStringExtra("boardType");

        //댓글
        commentAdapter = new CommentAdapter(this);
        getCommentFromDB();
        commentList = findViewById(R.id.commentListView);

        commentList.setAdapter(commentAdapter);
        commentList.setVerticalScrollBarEnabled(false);
    }

    public void goToPostModify(View view) { //수정 버튼 클릭 시
        Intent intent = new Intent(PostContent.this, PostModify.class);

        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("writeDate", writeDate);
        intent.putExtra("writer", writer);
        intent.putExtra("boardType", boardType);

        startActivity(intent);
        System.out.println("Move To Modify Post");
    }

    public void showDeleteDialog(View view) {    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(PostContent.this);
        dialog.setMessage("작성하신 글을 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                //DB에서 삭제
                String date = writeDate.substring(0, 4) + writeDate.substring(5, 7) + writeDate.substring(8, 10);
                if (boardType.equals("free"))
                    postRef = database.getReference("/자유게시판/" + date + "/");
                else
                    postRef = database.getReference("/익명게시판/" + date + "/");
                String time = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("아이디" + time, null);
                postRef.updateChildren(childUpdates);


                Toast toastView = Toast.makeText(PostContent.this, "삭제 완료", Toast.LENGTH_SHORT);
                toastView.show();
                finish();   //현재 액티비티 없애기
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(PostContent.this, "삭제 취소", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void goToBack(View view) {   //목록으로 버튼 클릭 시
        Toast toastView = Toast.makeText(this, "이전 페이지", Toast.LENGTH_SHORT);
        toastView.show();
        finish();   //현재 액티비티 없애기
    }

    public void addComment(View view) {  //댓글 등록 버튼 클릭 이벤트
        String commentContent = writtenCommentText.getText().toString();
        if (commentContent.equals("")) //내용을 작성하지 않은 경우
            Toast.makeText(this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
        else {
            String postCode = "아이디" + writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19);
            commentRef = database.getReference("/댓글/"+postCode+"/");

            //댓글 작성 일자
            Date time = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
            String addTime = format1.format(time);

            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> commentValues = null;

            Comment comment = new Comment("아이디", commentContent, addTime);
            commentValues = comment.toMap();
            System.out.println("commentValue- " + commentValues);

            SimpleDateFormat format2 = new SimpleDateFormat ( "HHmmss");
            String commentNum = "아이디" + format2.format(time);
            childUpdates.put(commentNum, commentValues);
            commentRef.updateChildren(childUpdates);
        }
    }

    private void getCommentFromDB(){    //DB에서 댓글 데이터 가져오기
        database = FirebaseDatabase.getInstance();
        String postCode = "아이디" + writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19);
        commentRef = database.getReference("/댓글/"+postCode+"/");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //하위 구조
                        commentAdapter.addItem(snap.getValue(Comment.class));
                    }
                commentAdapter.notifyDataSetChanged(); //리스트 새로고침 알림
                setListViewHeight();    //데이터를 다 받아온 후 리스트뷰 높이 계산, 적용
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    //리스트뷰 높이 계산 (ScrollView 안에 있기 때문에 높이 계산이 별도로 필요)
    private void setListViewHeight(){
        int totalHeight = 0;
        for (int i=0; i<commentAdapter.getCount(); i++){
            View listItem = commentAdapter.getView(i, null, commentList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = commentList.getLayoutParams();
        params.height = totalHeight + (commentList.getDividerHeight() * (commentAdapter.getCount() - 1));
        commentList.setLayoutParams(params);
        commentList.requestLayout();
    }
}
