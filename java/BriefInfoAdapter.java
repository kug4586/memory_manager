package com.example.android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BriefInfoAdapter
        extends RecyclerView.Adapter<BriefInfoAdapter.ViewHolder>
        implements OnBriefInfoClickListener {
    // BriefInfo 객체들을 담아 둘 공간 -> 어뎁터가 관리
    ArrayList<BriefInfo> items = new ArrayList<BriefInfo>();


    // OnBriefInfoClickListener 인터페이스
    OnBriefInfoClickListener listener;
    public void SetOnItemClickListener(OnBriefInfoClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void OnItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.OnItemClick(holder, view, position);
        }
    }

    // 어떤 아이템을 items 안에 저장
    public void AddItem(BriefInfo item) {
        items.add(item);
    }
    // 실행하는 위치의 items를 가져옴
    public void SetItems(ArrayList<BriefInfo> items) {
        this.items = items;
    }
    // itmes에서 아이템 꺼내기
    public BriefInfo GetItem(int position) {
        return items.get(position);
    }
    // items의 특정 위치에 아이템 저장
    public void SetItem(int position, BriefInfo item) {
        items.set(position, item);
    }
    // items 속 모든 아이템을 삭제
    public void RemoveAllItem() {
        items.clear();
    }
    // items 속 특정 아이템 삭제
    public void RemoveItem(int position) {
        items.remove(position);
    }


    // 뷰홀더가 생성될 때
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(viewGroup.getContext());
        View item_view = inflater.inflate(R.layout.brief_info, viewGroup, false);
        return new ViewHolder(item_view, this);
    }
    // 뷰홀더가 재사용 될 때
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BriefInfo item = items.get(position);
        holder.SetItem(item);
    }
    // 어뎁터에서 관리하는 아이템 수
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 뷰홀더 클래스
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 변수
        int[] colors = {
                R.drawable.red, R.drawable.orange,
                R.drawable.yellow, R.drawable.green,
                R.drawable.blue, R.drawable.darkblue,
                R.drawable.violet
        };
        TextView tag;
        ImageView color;
        // 생성자
        public ViewHolder(@NonNull View itemView, final OnBriefInfoClickListener listener) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
            color = itemView.findViewById(R.id.color);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.OnItemClick(ViewHolder.this, view, getAdapterPosition());
                    }
                }
            });
        }
        // 뷰홀더에 데이터 설정하기
        public void SetItem(BriefInfo item) {
            tag.setText(item.GetTag());
            color.setImageResource(colors[item.GetColor()]);
        }
    }
}
