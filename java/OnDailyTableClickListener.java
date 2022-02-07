package com.example.android_app;

import android.view.View;

// 일일 암기표가 클릭됬을 때, 그 이벤트를 전달받는 리스너
public interface OnDailyTableClickListener {
    public void onItemClick(DailyTableAdapter.ViewHolder holder, View view);
}
