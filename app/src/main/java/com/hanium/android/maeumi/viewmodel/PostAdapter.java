package com.hanium.android.maeumi.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<Post> items = new ArrayList<Post>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.board_item, viewGroup, false);

        return new ViewHolder(itemView);
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        TextView writerView;
        TextView dateView;

        public ViewHolder(View itemView){
            super(itemView);

            titleView = itemView.findViewById(R.id.postTitle);
            writerView = itemView.findViewById(R.id.postWriter);
            dateView = itemView.findViewById(R.id.postDate);
        }

        public void setItem(Post item){
            titleView.setText(item.getTitle());
            writerView.setText(item.getWriter());
            dateView.setText(item.getWriteDate());
        }
    }

    public void addItem(Post item){
        items.add(item);
    }

    public void setItems(ArrayList<Post> items){
        this.items = items;
    }

    public Post getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Post item){
        items.set(position, item);
    }
}