package com.hanium.android.maeumi.view.diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;

import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    public CalendarAdapter(ArrayList<String> days0fMonth, OnItemListener onItemListener) {
        this.days0fMonth = days0fMonth;
        this.onItemListener = onItemListener;
    }
//        public CalendarAdapter(ArrayList<String> days0fMonth) {
//        this.days0fMonth = days0fMonth;
//    }

    private final ArrayList<String> days0fMonth;
    private final OnItemListener onItemListener;

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height =(int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        holder.day0fMonth.setText(days0fMonth.get(position));
    }
    @Override
    public int getItemCount()
    {
        return days0fMonth.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}