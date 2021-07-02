package com.hanium.android.maeumi.view.diary;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;

import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> days0fMonth;
    private final OnItemListener onItemListener;
    public  ArrayList<String> testMonth = new ArrayList<>();

    public CalendarAdapter(ArrayList<String> days0fMonth, OnItemListener onItemListener) {
        this.days0fMonth = days0fMonth;
        this.onItemListener = onItemListener;
        testMonth.add("2");
        testMonth.add("21");
        testMonth.add("15");
    }

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
//        System.out.println("hello  - "+ testMonth);
//        System.out.println(days0fMonth.get(position).getClass());
        holder.day0fMonth.setText(days0fMonth.get(position));

        // 조건 비교 후 배경 색칠
        for (int i=0;i<testMonth.size();i++){
//            System.out.println("testMonth - "+ testMonth.get(i));
            if(days0fMonth.get(position).equals(testMonth.get(i))){
                holder.parentView.setBackgroundColor(Color.GRAY);
            }
        }
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