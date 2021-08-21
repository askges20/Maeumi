package com.hanium.android.maeumi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.interfaces.OnPostItemClickListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements OnPostItemClickListener {
    public String boardType;

    public static Post curPost; //가장 최근에 클릭한 게시글
    ArrayList<Post> items = new ArrayList<Post>();  //게시글 목록
    OnPostItemClickListener listener;

    public PostAdapter(String boardType) {
        this.boardType = boardType; //게시판 종류
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.board_item, viewGroup, false);

        return new ViewHolder(itemView, this, boardType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Post item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnPostItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String boardType;

        TextView titleView;
        TextView writerView;
        TextView dateView;
        ImageView postItemCommentImg;
        TextView postItemCommentCntText;
        TextView likeCntText;

        public ViewHolder(View itemView, final OnPostItemClickListener listener, String boardType) {
            super(itemView);

            this.boardType = boardType;

            titleView = itemView.findViewById(R.id.postTitle);
            writerView = itemView.findViewById(R.id.postWriter);
            dateView = itemView.findViewById(R.id.postDateText);
            postItemCommentImg = itemView.findViewById(R.id.postItemCommentImg);
            postItemCommentCntText = itemView.findViewById(R.id.postItemCommentCntText);

            //익명게시판은 댓글 표시 없음
            if (boardType.equals("anonymous")) {
                postItemCommentImg.setVisibility(View.GONE);
                postItemCommentCntText.setVisibility(View.GONE);
            }

            likeCntText = itemView.findViewById(R.id.postItemLikeCnt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Post item) {
            titleView.setText(item.getTitle());

            //익명게시판은 사용자 닉네임 표시 X
            if (boardType.equals("anonymous")) {
                writerView.setText("익명");
            } else {
                writerView.setText(item.getWriter());
            }

            dateView.setText(item.getWriteDate());
            postItemCommentCntText.setText(item.getCommentCnt() + "");
            likeCntText.setText(item.getLikeUsersCnt() + "");
        }
    }

    public void addItem(Post item) {
        item.setAdapter(this);  //각 아이템에 postAdapter 저장
        item.setLikeUsers(false);    //공감을 누른 사용자 DB로부터 읽어오기
        item.setCommentCnt();   //댓글 개수 DB로부터 읽어와서 저장
        items.add(0, item); //리스트에 추가, 최신글부터 정렬하도록
    }

    public void setItems(ArrayList<Post> items) {
        this.items = items;
    }

    public Post getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Post item) {
        items.set(position, item);
    }

    public void clearList() {
        items.clear();
    }
}
