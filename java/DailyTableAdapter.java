package com.example.android_app;

import android.animation.AnimatorSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

    // 어떤 아이템을 items 안에 저장
    public void addItem(DailyTable item) {
        items.add(item);
    }
    // 기존 items를 다시 설정
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
        TextView name;
        TextView count;
        ImageView clear_btn;
        LinearLayout front_card;
        LinearLayout back_card;
        // 생성자
        public ViewHolder(View itemView, final OnDailyTableClickListener listener) {
            super(itemView);
            // 카드뷰의 객체들
            name = itemView.findViewById(R.id.TableName);
            count = itemView.findViewById(R.id.CountOfRepeat);
            clear_btn = itemView.findViewById(R.id.ClearBtn);
            front_card = itemView.findViewById(R.id.FrontCard);
            back_card = itemView.findViewById(R.id.BackCard);
            // 객체가 클릭됐을 때
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view);
                    }
                }
            });
        }
        // 뷰홀더 안의 뷰 객체에서 데이터 뽑아내기
        public void setItem(DailyTable item) {
            name.setText(item.get_table_name());
            count.setText(item.get_count_of_repeat());
        }
    }
}
