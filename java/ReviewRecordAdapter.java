package com.example.android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecordAdapter
        extends RecyclerView.Adapter<ReviewRecordAdapter.ViewHolder>
        implements OnReviewRecordClickListener {
    // ReviewRecord 객체들을 담아 둘 공간 -> 어뎁터가 관리
    ArrayList<ReviewRecord> items = new ArrayList<ReviewRecord>();


    // OnReviewRecordClickListener 인터페이스
    OnReviewRecordClickListener listener_1;
    public void SetItemClickListener(OnReviewRecordClickListener listener) {
        this.listener_1 = listener;
    }
    @Override
    public void OnItemClick(ViewHolder holder, View view, int position) {
        if (listener_1 != null) {
            listener_1.OnItemClick(holder, view, position);
        }
    }


    // 어떤 아이템을 items 안에 저장
    public void AddItem(ReviewRecord item) {
        items.add(item);
    }
    // 실행하는 위치의 items를 가져옴
    public void SetItems(ArrayList<ReviewRecord> items) {
        this.items = items;
    }
    // itmes에서 특정 위치의 아이템 꺼내기
    public ReviewRecord GetItem(int position) {
        return items.get(position);
    }
    // items의 특정 위치에 아이템 저장
    public void SetItem(int position, ReviewRecord item) {
        items.set(position, item);
    }
    // items에서 특정 위치의 아이템 삭제
    public void RemoveItem(int position) {
        items.remove(position);
    }
    // items 속 모든 아이템을 삭제
    public void RemoveAllItem() {
        items.clear();
    }


    // 뷰홀더가 만들어질 때
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.review_record, viewGroup, false);
        return new ViewHolder(itemView, this);
    }
    // 뷰홀더가 재사용될 때
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewRecord item = items.get(position);
        holder.SetItem(item);
    }
    // 어뎁터에서 관리하는 아이템 수
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 뷰홀더 클래스
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 변수 설정
        int[] colors = {
                R.color.black, R.color.red, R.color.orange, R.color.yellow,
                R.color.green, R.color.blue, R.color.darkblue, R.color.violet
        };
        TextView table_name;
        CardView review_record;
        // 생성자
        public ViewHolder(@NonNull View itemView,
                          final OnReviewRecordClickListener listener_1) {
            super(itemView);
            // id로 객체 찾기
            table_name = itemView.findViewById(R.id.TableName_of_ReviewRecord);
            review_record = itemView.findViewById(R.id.ReviewRecord);
            // 객체가 클릭되었을 때
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener_1 != null) {
                        listener_1.OnItemClick(ViewHolder.this, view, getAdapterPosition());
                    }
                }
            });
        }
        // 뷰홀더에 데이터 설정하기
        public void SetItem(ReviewRecord item) {
            table_name.setText(item.GetTableName());
            review_record.setCardBackgroundColor(
                    this.itemView.getContext().getColor(colors[item.GetColor()]));
        }
    }
}
