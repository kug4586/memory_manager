package com.example.android_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewerAdapter
        extends RecyclerView.Adapter<ViewerAdapter.ViewHolder>
        implements OnCheckListener {
    // Viewer 객체들을 담아 둘 공간 -> 어댑터가 관리
    ArrayList<Viewer> items = new ArrayList<Viewer>();


    // OnCheckListener 리스너
    OnCheckListener listener;
    public void SetOnCheckListener(OnCheckListener listener) {
        this.listener = listener;
    }
    @Override
    public void OnCheck(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.OnCheck(holder, view, position);
        }
    }


    // 어떤 아이템을 items 안에 저장
    public void AddItem(Viewer item) {
        items.add(item);
    }


    // 뷰홀더가 새로 만들어질 때
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View item_view = inflater.inflate(R.layout.viewer, viewGroup, false);
        return new ViewHolder(item_view, this);
    }
    // 뷰홀더가 재사용될 때
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Viewer item = items.get(position);
        holder.SetDate(item);
        holder.SetReviewed(item);
    }
    // 어뎁터에서 관리하는 아이템 수
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 뷰홀더 클레스
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 변수 설정
        private boolean touchable = true;
        DatabaseHelper db_helper;
        TextView date;
        ImageView check;
        // 생성자
        public ViewHolder(@NonNull View itemView, final OnCheckListener listener) {
            super(itemView);
            // DB 헬퍼 불러오기
            db_helper = new DatabaseHelper(itemView.getContext());
            // 카트뷰의 객체들
            date = itemView.findViewById(R.id.date);
            check = itemView.findViewById(R.id.check);
            // 체크란이 클릭됐을 때
            check.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (IsToday(date.getText().toString()) && touchable && listener != null) {
                        listener.OnCheck(ViewHolder.this, view, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
        // 뷰홀더에 날짜 설정하기
        public void SetDate(Viewer item) {
            date.setText(item.GetDate());
        }
        // 뷰홀더에 복습 체크 설정하기
        public void SetReviewed(Viewer item) {
            if (item.GetChecked()) {
                check.setImageResource(R.drawable.check);
                touchable = false;
            }
        }
        // 날짜가 오늘이 맞는가
        public boolean IsToday(String date) {
            Date today = new Date();
            SimpleDateFormat date_format = new SimpleDateFormat("MM/dd");
            if (date.contains(date_format.format(today))) {
                return true;
            } else {
                return false;
            }
        }
        // 체크하기
        public void Checking(View view) {
            check.setVisibility(View.INVISIBLE);
            check.setImageResource(R.drawable.check);
            Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.check);
            check.startAnimation(anim);
            check.setVisibility(View.VISIBLE);
            touchable = false;
        }
    }
}
