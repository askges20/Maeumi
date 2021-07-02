package com.hanium.android.maeumi.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Comment> items = new ArrayList<Comment>();

    public CommentAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public int getItemCount() {
        return items.size();
    }

    public void addItem(Comment item){
        items.add(item);
    }

    public void setItems(ArrayList<Comment> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public Comment getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.comment_item, null);
        TextView writer = view.findViewById(R.id.commentWriterText);
        TextView content = view.findViewById(R.id.commentContentText);
        TextView writeDate = view.findViewById(R.id.commentWriteDate);

        writer.setText(items.get(position).getWriter());
        content.setText(items.get(position).getContent());
        writeDate.setText(items.get(position).getWriteDate());

        TextView deleteBtn = view.findViewById(R.id.commentDeleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), position+" : "+getItem(position).getContent(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setItem(int position, Comment item){
        items.set(position, item);
    }

    public void clearList(){
        items.clear();
    }

}
