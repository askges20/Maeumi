package com.hanium.android.maeumi.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.view.diary.CalendarViewHolder;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> days0fMonth;
    private final OnItemListener onItemListener;
    private static ArrayList<String> diaryDates = new ArrayList<>();
    private static ArrayList<String> weekends = new ArrayList<>();
    private String compareDate;

    public CalendarAdapter(ArrayList<String> weekends,ArrayList<String> days0fMonth, OnItemListener onItemListener) {
        this.weekends = weekends;
        this.days0fMonth = days0fMonth;
        this.onItemListener = onItemListener;
    }

    public void SetDates(ArrayList<String> data) {
        this.diaryDates = data;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.day0fMonth.setText(days0fMonth.get(position));

        for (int i = 0;i<weekends.size();i++){
            if(weekends.get(i) == days0fMonth.get(position) ){
                holder.day0fMonth.setTextColor(Color.RED);
            }
        }

        // 1 ~ 9일 까지 앞에 0 붙이기
        if (days0fMonth.get(position).length() == 1) {
            compareDate = "0" + days0fMonth.get(position);
        } else {
            compareDate = days0fMonth.get(position);
        }

        //diaryDates 안에 있는 날짜면 배경 변경
        if (diaryDates.contains(compareDate+"1")){
            holder.parentView.setBackgroundResource(R.drawable.diary_glad);
        } else if (diaryDates.contains(compareDate+"2")){
            holder.parentView.setBackgroundResource(R.drawable.diary_happy);
        } else if (diaryDates.contains(compareDate+"3")){
            holder.parentView.setBackgroundResource(R.drawable.diary_calm);
        }else if (diaryDates.contains(compareDate+"4")){
            holder.parentView.setBackgroundResource(R.drawable.diary_angry);
        } else if (diaryDates.contains(compareDate+"5")){
            holder.parentView.setBackgroundResource(R.drawable.diary_sad);
        }else if (diaryDates.contains(compareDate+"6")){
            holder.parentView.setBackgroundResource(R.drawable.diary_worried);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CalendarViewHolder holder){
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return days0fMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}