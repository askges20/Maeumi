package com.hanium.android.maeumi.view.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hanium.android.maeumi.view.loading.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.PostAdapter;
import com.hanium.android.maeumi.model.Comment;
import com.hanium.android.maeumi.adapters.CommentAdapter;
import com.hanium.android.maeumi.model.Post;

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

    Post post = PostAdapter.curPost;    //클릭한 게시글 객체
    String title, content, writeDate, writer, writerUid, boardType;
    String postCode;    //게시글 번호

    ImageButton dropDownBtn;    //드롭다운 메뉴 버튼

    TextView titleText, contentText, dateText, writerText; //제목, 내용, 날짜, 작성자 텍스트

    ListView commentList; //댓글 목록
    LinearLayout writeCommentArea;  //댓글 작성 영역
    EditText writtenCommentText;    //댓글 작성칸
    Button addCommentBtn;   //댓글 등록 버튼

    InputMethodManager imm; //키보드 제어

    ImageView likeHeartImg; //공감 버튼 하트 이미지
    TextView likeCntText;   //공감 수 텍스트

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

        likeHeartImg = findViewById(R.id.likeHeartImg);
        likeCntText = findViewById(R.id.likeCnt);

        Intent prevIntent = getIntent();
        boardType = prevIntent.getStringExtra("boardType");

        title = post.getTitle();
        titleText.setText(title);
        content = post.getContent();
        contentText.setText(content);
        writeDate = post.getWriteDate();
        dateText.setText(writeDate);
        writer = post.getWriter();
        if (boardType.equals("anonymous")) { //익명게시판 글은 닉네임 표시 X
            writerText.setText("익명");
        } else {
            writerText.setText(writer);
        }
        writerUid = post.getWriterUid();
        likeCntText.setText(post.getLikeUsersCnt() + "");

        postCode = writeDate.substring(11, 13) + writeDate.substring(14, 16) + writeDate.substring(17, 19) + writerUid;

        commentList = findViewById(R.id.commentListView);
        writeCommentArea = findViewById(R.id.writeCommentArea);

        //익명게시판 글 -> 댓글 없음
        if (boardType.equals("anonymous")) {
            commentList.setVisibility(View.GONE);
            writeCommentArea.setVisibility(View.GONE);
        }

        String userUid = LoginUser.getInstance().getUid();
        if (post.getLikeUsers().contains(userUid)) {  //이미 공감을 눌렀던 사용자이면
            likeHeartImg.setImageResource(R.drawable.heart_icon_2);
        } else {    //공감을 누르지 않았던 사용자이면
            likeHeartImg.setImageResource(R.drawable.heart_icon_1);
        }


        if (!LoginUser.getInstance().getUid().equals(writerUid)) { //로그인한 사용자와 글 작성자의 Uid가 다르면
            dropDownBtn.setVisibility(View.GONE);
        }

        //댓글
        commentAdapter = new CommentAdapter(this, this);
        getCommentFromDB();

        commentList.setAdapter(commentAdapter);
        commentList.setVerticalScrollBarEnabled(false);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    //드롭다운 메뉴 클릭 시
    public void onClickDropDownMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
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
        AlertDialog dialog = new AlertDialog.Builder(PostContent.this)
        .setMessage("작성하신 글을 삭제하시겠습니까?")
        .setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                //DB에서 게시글 데이터 삭제
                String date = writeDate.substring(0, 4) + writeDate.substring(5, 7) + writeDate.substring(8, 10);
                if (boardType.equals("free"))
                    postRef = database.getReference("/자유게시판/" + date + "/");
                else
                    postRef = database.getReference("/익명게시판/" + date + "/");
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(postCode, null);
                postRef.updateChildren(childUpdates);


                //DB에서 해당 게시글 댓글 삭제
                commentRef = database.getReference("/댓글/" + postCode);
                commentRef.setValue(null);


                //DB에서 해당 게시글 공감 삭제
                DatabaseReference likeRef = database.getReference("/공감/" + postCode);
                likeRef.setValue(null);


//                Toast toastView = Toast.makeText(PostContent.this, "삭제 완료", Toast.LENGTH_SHORT);
//                toastView.show();
                finish();   //현재 액티비티 없애기
            }
        })
        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(PostContent.this, "삭제 취소", Toast.LENGTH_SHORT).show();
            }
        })
        .show();

        //폰트 크기 조정
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(18);
    }

    public void goToBack(View view) {   //뒤로가기 버튼 클릭 시
        finish();   //현재 액티비티 없애기
    }

    public void addComment(View view) {  //댓글 등록 버튼 클릭 이벤트
        String commentContent = writtenCommentText.getText().toString();
        if (commentContent.equals("")) //내용을 작성하지 않은 경우
            Toast.makeText(this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
        else {
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
    public void deleteComment(Comment comment) {
        AlertDialog dialog = new AlertDialog.Builder(this)
        .setMessage("댓글을 삭제하시겠습니까?")
        .setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                processDeleteComment(comment);  //댓글 삭제 진행
            }
        })
        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //댓글 삭제 취소
            }
        })
        .show();

        //폰트 크기 조정
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(18);
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

    //공감 버튼을 클릭했을 때
    public void onClickLikeBtn(View view) {
        String userUid = LoginUser.getInstance().getUid();
        if (post.getLikeUsers().contains(userUid)) {  //이미 공감을 눌렀던 사용자이면
            likeHeartImg.setImageResource(R.drawable.heart_icon_1);
            post.removeLikeUser(userUid);
            //공감 취소
        } else {    //공감을 누르지 않았던 사용자이면
            likeHeartImg.setImageResource(R.drawable.heart_icon_2);
            post.addLikeUser(userUid);
        }
        likeCntText.setText(post.getLikeUsersCnt() + "");
    }
}
