package com.hanium.android.maeumi;

import android.view.View;

import com.hanium.android.maeumi.viewmodel.PostAdapter;

public interface OnPostItemClickListener {
    public void onItemClick(PostAdapter.ViewHolder holder, View view, int position);
}
