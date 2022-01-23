package com.example.android_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class HomeFragment extends Fragment {

    // 인터페이스 : fragment의 정보를 Activity로
    public static interface HomeCallback {
        public void change_menu();
    }
    public HomeCallback callback;

    public boolean is_menu_close = true;

    Animation transparent_on;
    Animation transparent_off;

    DailyTableAdapter adapter;

    ImageView menu_button;
    ImageView setting_button;
    RecyclerView daily_tables_screen;
    View hahaha;

    // 프래그먼트가 액티비티와 연결될 때
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeCallback) {
            callback = (HomeCallback) context;
        }
    }

    // 프래그먼트의 뷰를 전달함
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // id로 뷰 객체 불러오기
        menu_button = rootView.findViewById(R.id.menu_button);
        setting_button = rootView.findViewById(R.id.setting_button);
        daily_tables_screen = rootView.findViewById(R.id.daily_tables_screen);
        hahaha = rootView.findViewById(R.id.hahaha);

        // 애니메이션
        transparent_on = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.transparent_on);
        transparent_off = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.transparent_off);
        anim_listener listener = new anim_listener();
        transparent_on.setAnimationListener(listener);
        transparent_off.setAnimationListener(listener);

        hahaha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 메뉴 버튼
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.change_menu();
                    set_hahaha();
                }
            }
        });

        // 설정 버튼
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 일일 암기표
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        daily_tables_screen.setLayoutManager(manager);
        adapter = new DailyTableAdapter();

        adapter.addItem(new DailyTable("2021.11", "1st"));
        adapter.addItem(new DailyTable("2020.11", "1st"));
        adapter.addItem(new DailyTable("2019.11", "1st"));
        adapter.addItem(new DailyTable("2018.11", "1st"));
        adapter.addItem(new DailyTable("2017.11", "1st"));
        adapter.addItem(new DailyTable("2016.11", "1st"));
        adapter.addItem(new DailyTable("2015.11", "1st"));

        daily_tables_screen.setAdapter(adapter);

        return rootView;
    }

    private class anim_listener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationEnd(Animation animation) {
            if (!is_menu_close) {
                hahaha.setVisibility(View.GONE);
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    public void button_on_off() {
        if (is_menu_close) {
            menu_button.setEnabled(false);
            is_menu_close = false;
        } else {
            menu_button.setEnabled(true);
            is_menu_close = true;
        }
    }

    public void set_hahaha() {
        if (is_menu_close) {
            hahaha.setVisibility(View.VISIBLE);
            hahaha.startAnimation(transparent_on);
        } else {
            hahaha.startAnimation(transparent_off);
        }
    }
}
