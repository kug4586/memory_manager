package com.example.android_app;

import android.view.View;

public interface OnDailyTableClickListener {
    public void onItemClick(DailyTableAdapter.ViewHolder holder, View view);
    public void OnLongClick(DailyTableAdapter.ViewHolder holder, View view, int position);
}
