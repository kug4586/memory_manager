package com.example.android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewerAdapter
        extends RecyclerView.Adapter<ViewerAdapter.ViewHolder>
        implements OnViewerClickListener {
    // Viewer 객체들을 담아 둘 공간 -> 어댑터가 관리
    ArrayList<Viewer> items = new ArrayList<Viewer>();
    // OnViewerClickListener 인터페이스
    OnViewerClickListener listener;
    public void SetOnItemClickListener(OnViewerClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(DailyTableAdapter.ViewHolder holder, View view) {
        if (listener != null) {
            listener.onItemClick(holder, view);
        }
    }

    // 어떤 아이템을 items 안에 저장
    public void AddItem(Viewer item) {
        items.add(item);
    }
    // 실행하는 위치의 items를 가져옴
    public void SetItems(ArrayList<Viewer> items) {
        this.items = items;
    }
    // itmes에서 아이템 꺼내기
    public Viewer GetItem(int position) {
        return items.get(position);
    }
    // items의 특정 위치에 아이템 저장
    public void SetItem(int position, Viewer item) {
        items.set(position, item);
    }
    // items 속 모든 아이템을 삭제
    public void RemoveAllItem() {
        items.clear();
    }
    // items 속 특정 아이템 삭제
    public void RemoveItem(String item) {
        int index = items.indexOf(item);
        items.remove(index);
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
        holder.SetItem(item);
    }
    // 어뎁터에서 관리하는 아이템 수
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 뷰홀더 클레스
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 변수 설정
        TextView date;
        ImageView check;
        // 생성자
        public ViewHolder(@NonNull View itemView, final OnViewerClickListener listener) {
            super(itemView);
            // 카트뷰의 객체들
            date = itemView.findViewById(R.id.date);
            check = itemView.findViewById(R.id.check);
            // 체크란이 클릭됐을 때
        }
        // 뷰홀더에 데이터 설정하기
        public void SetItem(Viewer item) {
            date.setText(item.GetDate());
        }
    }
}
