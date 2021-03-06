package com.hanium.android.maeumi.view.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.PostAdapter;
import com.hanium.android.maeumi.interfaces.OnPostItemClickListener;
import com.hanium.android.maeumi.model.Post;

public class BoardQuestionFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference questionBoardRef;
    PostAdapter postAdapter;

    Board board;    //Intent

    public BoardQuestionFragment(Board board) {
        this.board = board;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //레이아웃 연결
        View view = inflater.inflate(R.layout.board_question_fragment, container, false);
        Context context = view.getContext();


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.boardQuestionRecyclerView);
        recyclerView.setHasFixedSize(true);

        //리사이클러뷰 - 레이아웃 매니저로 배치 방법 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter("question");


        getPostFromDB(); //DB에서 게시글 데이터 조회
        recyclerView.setAdapter(postAdapter);   //리사이클러뷰에 어댑터 등록
        addListener();  //리스너 추가

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //Firebase에서 데이터 가져오기
    private void getPostFromDB() {
        database = FirebaseDatabase.getInstance();
        questionBoardRef = database.getReference("/게시판/질문게시판/");
        questionBoardRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postAdapter.clearList();    //데이터 중복 출력 문제 해결을 위해 리스트 초기화 후 다시 받아옴
                for (DataSnapshot dateSnap : dataSnapshot.getChildren()) { //하위 구조 (작성일자)
                    for (DataSnapshot snap : dateSnap.getChildren()) { //하위 구조 (게시글)
                        postAdapter.addItem(snap.getValue(Post.class));
                    }
                }
                postAdapter.notifyDataSetChanged(); //리스트 새로고침 알림
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    //카드별 클릭 이벤트 추가
    private void addListener() {
        postAdapter.setOnItemClickListener(new OnPostItemClickListener() {
            @Override
            public void onItemClick(PostAdapter.ViewHolder holder, View view, int position) {
                Post item = postAdapter.getItem(position);
                goToPostContent(item);
            }
        });
    }

    //게시글 내용 페이지로 이동
    private void goToPostContent(Post item) {
        Intent intent = new Intent(board, PostContent.class);
        postAdapter.curPost = item;
        intent.putExtra("boardType", "question");
        startActivity(intent);
    }
}
