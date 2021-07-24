package com.hanium.android.maeumi.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<Message> messageList;
    private Activity activity;

    public ChatAdapter(List<Message> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_message_one, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = messageList.get(position);

        String content = message.getContent();
        boolean isReceived = message.getIsReceived();
        boolean isFirstMsgOfDay = message.isFirstMsgOfDay();
        int year = message.getYear();
        int month = message.getMonth();
        int date = message.getDate();

        if (isReceived) { //bot으로부터 온 메세지이면
            holder.chatMaeumiImg.setVisibility(View.VISIBLE);
            holder.messageReceive.setVisibility(View.VISIBLE);
            holder.messageSend.setVisibility(View.GONE);
            holder.messageReceive.setText(content);
        } else { //사용자가 보낸 메세지이면
            holder.messageSend.setVisibility(View.VISIBLE);
            holder.chatMaeumiImg.setVisibility(View.GONE);
            holder.messageReceive.setVisibility(View.GONE);
            holder.messageSend.setText(content);
        }

        //해당 날짜의 첫번째 메세지라면 날짜 표시
        if (isFirstMsgOfDay) {
            holder.chatDateText.setText(year + "년 " + month + "월 " + date + "일");
        } else {
            holder.chatDateText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView chatDateText;
        TextView messageSend;
        TextView messageReceive;
        ImageView chatMaeumiImg;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chatDateText = itemView.findViewById(R.id.chatDateText);
            messageSend = itemView.findViewById(R.id.message_send);
            messageReceive = itemView.findViewById(R.id.message_receive);
            chatMaeumiImg = itemView.findViewById(R.id.chatMaeumiImg);
        }
    }
}
