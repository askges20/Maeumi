package com.hanium.android.maeumi.view.board;

import android.content.Context;
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
import com.hanium.android.maeumi.model.Post;
import com.hanium.android.maeumi.viewmodel.PostAdapter;

/*자유게시판 fragment*/
public class BoardFreeFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference freeBoardRef;
    PostAdapter adapter = new PostAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //레이아웃 연결
        View view = inflater.inflate(R.layout.board_free_fragment, container, false);
        Context context = view.getContext();


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.boardFreeRecyclerView);
        recyclerView.setHasFixedSize(true);

        //리사이클러뷰 - 레이아웃 매니저로 배치 방법 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        getPostFromDB(); //DB에서 게시글 데이터 조회
        recyclerView.setAdapter(adapter);   //리사이클러뷰에 어댑터 등록

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //Firebase에서 데이터 가져오기
    private void getPostFromDB(){
        database = FirebaseDatabase.getInstance();
        freeBoardRef = database.getReference("/자유게시판/");
        freeBoardRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dateSnap : dataSnapshot.getChildren()) { //하위 구조 (작성일자)
                    for (DataSnapshot snap : dateSnap.getChildren()) { //하위 구조 (게시글)
                        adapter.addItem(snap.getValue(Post.class));
                    }
                }
                adapter.notifyDataSetChanged(); //리스트 새로고침 알림
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }
}
