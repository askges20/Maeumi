package com.hanium.android.maeumi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.interfaces.OnPostItemClickListener;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements OnPostItemClickListener {
    public static Post curPost; //가장 최근에 클릭한 게시글
    ArrayList<Post> items = new ArrayList<Post>();  //게시글 목록
    OnPostItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.board_item, viewGroup, false);

        return new ViewHolder(itemView, this);
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
        TextView titleView;
        TextView writerView;
        TextView dateView;
        TextView likeCntText;

        public ViewHolder(View itemView, final OnPostItemClickListener listener) {
            super(itemView);

            titleView = itemView.findViewById(R.id.postTitle);
            writerView = itemView.findViewById(R.id.postWriter);
            dateView = itemView.findViewById(R.id.postDateText);
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
            writerView.setText(item.getWriter());
            dateView.setText(item.getWriteDate());
            likeCntText.setText(item.getLikeUsersCnt()+"");
            System.out.println("셋아이템");
        }
    }

    public void addItem(Post item) {
        item.setAdapter(this);  //각 아이템에 postAdapter 저장
        item.setLikeUsers();    //공감을 누른 사용자 DB로부터 읽어오기
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

    public void clearList(){
        items.clear();
    }
}
