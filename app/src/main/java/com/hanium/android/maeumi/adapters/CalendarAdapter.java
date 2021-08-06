package com.hanium.android.maeumi.adapters;

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
    private String compareDate;

    public CalendarAdapter(ArrayList<String> days0fMonth, OnItemListener onItemListener) {
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

        // 1 ~ 9일 까지 앞에 0 붙이기
        if (days0fMonth.get(position).length() == 1) {
            compareDate = "0" + days0fMonth.get(position);
        } else {
            compareDate = days0fMonth.get(position);
        }
        // 조건 비교 후 배경 색칠
        for (int i = 0; i < diaryDates.size(); i++) {
            if (compareDate.equals(diaryDates.get(i).substring(0, 2))) {
                switch (diaryDates.get(i).substring(diaryDates.get(i).length() - 1)) {
                    case "1":
                        holder.parentView.setBackgroundResource(R.drawable.diary_good);
                        break;
                    case "2":
                        holder.parentView.setBackgroundResource(R.drawable.diary_normal);
                        break;
                    case "3":
                        holder.parentView.setBackgroundResource(R.drawable.diary_bad);
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return days0fMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}