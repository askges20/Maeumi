package com.hanium.android.maeumi.interfaces;

import android.view.View;

import com.hanium.android.maeumi.adapters.PostAdapter;

public interface OnPostItemClickListener {
    public void onItemClick(PostAdapter.ViewHolder holder, View view, int position);
}
