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

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;
import com.hanium.android.maeumi.viewmodel.PostAdapter;

/*자유게시판 fragment*/
public class BoardAnonymousFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //익명게시판 레이아웃 연결
        View view = inflater.inflate(R.layout.board_anonymous_fragment, container, false);
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.boardAnonymousRecyclerView);
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저로 배치 방법 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //어댑터를 이용해서 리사이클러뷰에 데이터 넘김
        //추후 firebase 데이터와 연결할 것
        PostAdapter adapter = new PostAdapter();
        for (int i = 0; i < 10; i++)
            adapter.addItem(new Post("제목:익명게시판", "내용","작성자", "작성일자"));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
