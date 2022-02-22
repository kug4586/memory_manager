package com.example.android_app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class DailyTableAdapter
        extends RecyclerView.Adapter<DailyTableAdapter.ViewHolder>
        implements OnDailyTableClickListener {
    // DailyTable 객체들을 담아 둘 공간 -> 어댑터가 관리
    ArrayList<DailyTable> items = new ArrayList<DailyTable>();


    // OnDailyTableClickListener 인터페이스
    OnDailyTableClickListener listener;
    public void setOnItemClickListener(OnDailyTableClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(ViewHolder holder, View view) {
        if (listener != null) {
            listener.onItemClick(holder, view);
        }
    }
    @Override
    public void OnLongClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.OnLongClick(holder, view, position);
        }
    }

    // 어떤 아이템을 items 안에 저장
    public void addItem(DailyTable item) {
        items.add(item);
    }
    // 실행하는 위치의 items를 가져옴
    public void setItems(ArrayList<DailyTable> items) {
        this.items = items;
    }
    // itmes에서 아이템 꺼내기
    public DailyTable getItem(int position) {
        return items.get(position);
    }
    // items의 특정 위치에 아이템 저장
    public void setItem(int position, DailyTable item) {
        items.set(position, item);
    }
    // items 속 모든 아이템을 삭제
    public void RemoveAllItem() {
        items.clear();
    }
    // items 속 특정 아이템 삭제
    public void RemoveItem(int position) {
        items.remove(position);
        // 변경점을 갱신하기
        notifyItemRemoved(position);
    }


    // 뷰홀더가 새로 만들어질 때, 뷰 객체를 새로 만듦
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.daily_table, viewGroup, false);
        return new ViewHolder(itemView, this);
    }
    // 뷰홀더가 재사용될 때, 기존의 뷰 객체를 재사용
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyTable item = items.get(position);
        holder.setItem(item);
    }
    // 어댑터에서 관리하는 아이템 개수
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 뷰홀더 클래스
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 변수 설정
        protected boolean is_flipped = false;

        TextView name;
        TextView count;
        ImageView clear_btn;
        ImageView review_img;
        LinearLayout front_card;
        FrameLayout back_card;

        // 생성자
        public ViewHolder(View itemView, final OnDailyTableClickListener listener) {
            super(itemView);
            // 카드뷰의 객체들
            name = itemView.findViewById(R.id.TableName_of_DailyTable);
            count = itemView.findViewById(R.id.CountOfRepeat);
            clear_btn = itemView.findViewById(R.id.ClearBtn);
            review_img = itemView.findViewById(R.id.ReviewImg);
            front_card = itemView.findViewById(R.id.FrontCard);
            back_card = itemView.findViewById(R.id.BackCard);

            // 앞면인 상태에서 클릭했을 때
            front_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view);
                    }
                }
            });

            // 복습 체크 버튼을 눌렀을 때
            clear_btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.OnLongClick(ViewHolder.this, view, getAdapterPosition());
                    }
                    return false;
                }
            });
        }

        // 뷰홀더에 데이터 설정하기
        public void setItem(DailyTable item) {
            name.setText(item.get_table_name());
            count.setText(item.get_count_of_repeat());
        }
    }
}
