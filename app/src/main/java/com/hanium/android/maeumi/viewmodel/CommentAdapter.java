package com.hanium.android.maeumi.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanium.android.maeumi.LoginUser;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Comment;
import com.hanium.android.maeumi.view.board.PostContent;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    PostContent postContent;

    ArrayList<Comment> items = new ArrayList<Comment>();

    public CommentAdapter(Context context, PostContent postContent) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.postContent = postContent;
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
        TextView writerText = view.findViewById(R.id.commentWriterText);
        TextView contentText = view.findViewById(R.id.commentContentText);
        TextView writeDateText = view.findViewById(R.id.commentWriteDate);

        Comment comment = items.get(position);  //해당 댓글
        writerText.setText(comment.getWriter());
        contentText.setText(comment.getContent());
        writeDateText.setText(comment.getWriteDate().substring(0, 10));

        TextView deleteBtn = view.findViewById(R.id.commentDeleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), position+" : "+getItem(position).getContent(), Toast.LENGTH_SHORT).show();
                postContent.deleteComment(getItem(position));
            }
        });

        //자신이 작성한 댓글이 아니면 삭제 버튼이 없음
        if (!LoginUser.getInstance().getUid().equals(comment.getWriterUid())){
            deleteBtn.setVisibility(View.GONE);
        }

        return view;
    }

    public void setItem(int position, Comment item){
        items.set(position, item);
    }

    public void clearList(){
        items.clear();
    }

}
