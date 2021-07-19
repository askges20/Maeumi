package com.hanium.android.maeumi.view.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Comment;
import com.hanium.android.maeumi.adapters.CommentAdapter;

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

    String title, content, writeDate, writer, writerUid, boardType;

    ListView commentList; //댓글 목록

    ImageButton dropDownBtn;    //드롭다운 메뉴 버튼

    TextView titleText; //제목 텍스트
    TextView contentText;   //내용 텍스트
    TextView dateText;  //날짜 텍스트
    TextView writerText;    //작성자 텍스트

    Button modifyPostBtn;   //게시글 수정 버튼
    Button deletePostBtn;   //게시글 삭제 버튼

    EditText writtenCommentText;    //댓글 작성칸
    Button addCommentBtn;   //댓글 등록 버튼

    InputMethodManager imm; //키보드 제어

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        PostContent_Activity = this;

        database = FirebaseDatabase.getInstance();

        dropDownBtn = findViewById(R.id.postContentMenu);

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
        writerUid = prevIntent.getStringExtra("writerUid");
        boardType = prevIntent.getStringExtra("boardType");

        if (!LoginUser.getInstance().getUid().equals(writerUid)){ //로그인한 사용자와 글 작성자의 Uid가 다르면
            dropDownBtn.setVisibility(View.GONE);
            modifyPostBtn.setVisibility(View.GONE); //수정 버튼 없음
            deletePostBtn.setVisibility(View.GONE); //삭제 버튼 없음
        }

        //댓글
        commentAdapter = new CommentAdapter(this, this);
        getCommentFromDB();
        commentList = findViewById(R.id.commentListView);

        commentList.setAdapter(commentAdapter);
        commentList.setVerticalScrollBarEnabled(false);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    //드롭다운 메뉴 클릭 시
    public void onClickDropDownMenu(View view) {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.post_content_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.post_modify_menu) {    //수정 버튼 클릭
                    goToPostModify();
                } else {    //삭제 버튼 클릭
                    showDeleteDialog();
                }

                return false;
            }
        });
        popupMenu.show();
    }

    public void goToPostModify() { //수정 버튼 클릭 시
        Intent intent = new Intent(PostContent.this, PostModify.class);

        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("writeDate", writeDate);
        intent.putExtra("writer", writer);
        intent.putExtra("boardType", boardType);

        startActivity(intent);
        System.out.println("Move To Modify Post");
    }

    public void showDeleteDialog() {    //삭제 버튼 클릭 시
        AlertDialog.Builder dialog = new AlertDialog.Builder(PostContent.this);
        dialog.setMessage("작성하신 글을 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                String loginUserUid = LoginUser.getInstance().getUid();  //로그인한 사용자의 Uid

                //DB에서 삭제
                String date = writeDate.substring(0, 4) + writeDate.substring(5, 7) + writeDate.substring(8, 10);
                if (boardType.equals("free"))
                    postRef = database.getReference("/자유게시판/" + date + "/");
                else
                    postRef = database.getReference("/익명게시판/" + date + "/");
                String time = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(time + loginUserUid, null);
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
            String postCode = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19) + writerUid;
            commentRef = database.getReference("/댓글/" + postCode + "/");

            //댓글 작성 일자
            Date time = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = format1.format(time);

            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> commentValues = null;

            LoginUser loginUser = LoginUser.getInstance();
            Comment comment = new Comment(loginUser.getName(), commentContent, addTime, loginUser.getUid());
            commentValues = comment.toMap();
            System.out.println("commentValue- " + commentValues);

            SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
            String commentNum = format2.format(time) + LoginUser.getInstance().getUid();
            childUpdates.put(commentNum, commentValues);
            commentRef.updateChildren(childUpdates);

            writtenCommentText.setText(""); //댓글 작성 칸 비우기
            imm.hideSoftInputFromWindow(writtenCommentText.getWindowToken(), 0);    //키보드 닫기
        }
    }

    private void getCommentFromDB() {    //DB에서 댓글 데이터 가져오기
        database = FirebaseDatabase.getInstance();
        String postCode = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19) + writerUid;
        commentRef = database.getReference("/댓글/" + postCode + "/");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentAdapter.clearList(); //데이터 중복 출력 문제 해결을 위해 리스트 초기화 후 다시 받아옴
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
    private void setListViewHeight() {
        int totalHeight = 0;
        for (int i = 0; i < commentAdapter.getCount(); i++) {
            View listItem = commentAdapter.getView(i, null, commentList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = commentList.getLayoutParams();
        params.height = totalHeight + (commentList.getDividerHeight() * (commentAdapter.getCount() - 1));
        commentList.setLayoutParams(params);
        commentList.requestLayout();
    }

    //댓글 삭제 버튼 클릭 시
    public void deleteComment(Comment comment){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("댓글을 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                processDeleteComment(comment);  //댓글 삭제 진행
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //댓글 삭제 취소
            }
        });
        dialog.show();
    }

    public void processDeleteComment(Comment comment) { //삭제할 댓글을 파라미터로 받음

        database = FirebaseDatabase.getInstance();
        String postCode = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19) + writerUid;
        commentRef = database.getReference("/댓글/" + postCode + "/");
        Map<String, Object> childUpdates = new HashMap<>();

        String commentDate = comment.writeDate;
        String commentNum = commentDate.substring(11, 13) + commentDate.substring(14, 16) + commentDate.substring(17, 19) + LoginUser.getInstance().getUid();

        childUpdates.put(commentNum, null); //DB에서 삭제
        commentRef.updateChildren(childUpdates);
        commentAdapter.notifyDataSetChanged();
    }
}
