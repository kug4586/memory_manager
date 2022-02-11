package com.example.android_app;

import android.view.View;

// ReviewRecord 객체가 클릭됬을 때, 그 이벤트를 전달받는 리스너
public interface OnReviewRecordClickListener {
    public void OnItemClick(ReviewRecordAdapter.ViewHolder holder, View view, int position);
}
