package com.hanium.android.maeumi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.model.Question;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{
    ArrayList<Question> items = new ArrayList<Question>();

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.test_item, viewGroup, false);

        return new QuestionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder viewHolder, int position) {
        Question item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView quesitonText;

        public ViewHolder(View itemView){
            super(itemView);
            quesitonText = itemView.findViewById(R.id.quesitonText);
        }

        public void setItem(Question item){
            quesitonText.setText(""+item.getNum()+". "+item.getContent());
        }
    }

    public void addItem(Question item){
        items.add(item);
    }

    public void setItems(ArrayList<Question> items){
        this.items = items;
    }

    public Question getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Question item){
        items.set(position, item);
    }
}
