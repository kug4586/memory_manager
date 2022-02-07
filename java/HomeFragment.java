package com.example.android_app;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    // 인터페이스 : fragment의 정보를 Activity로
    public static interface HomeCallback {
        public void change_menu();
    }
    public HomeCallback callback;

    public boolean is_menu_close = true;
    private Context ct;

    Animation transparent_on;
    Animation transparent_off;
    AnimatorSet flip_in;
    AnimatorSet flip_out;
    AnimatorSet past_flip_in;
    AnimatorSet past_flip_out;

    DailyTableAdapter adapter;
    DailyTableAdapter.ViewHolder prior_holder;

    ImageView menu_button;
    ImageView setting_button;
    RecyclerView daily_tables_screen;
    View hahaha;

    DatabaseHelper db_helper;

    // 프래그먼트가 액티비티와 연결될 때
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ct = context.getApplicationContext();
        if (context instanceof HomeCallback) {
            callback = (HomeCallback) context;
        }
        db_helper = new DatabaseHelper(context);
    }

    // 프래그먼트의 뷰를 전달함
    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // id로 뷰 객체 불러오기
        menu_button = rootView.findViewById(R.id.menu_button);
        setting_button = rootView.findViewById(R.id.setting_button);
        daily_tables_screen = rootView.findViewById(R.id.daily_tables_screen);
        hahaha = rootView.findViewById(R.id.hahaha);

        // 해상도 값 가져오기
        Float scale = ct.getResources().getDisplayMetrics().density;

        // 애니메이션
        transparent_on = AnimationUtils.loadAnimation(ct, R.anim.transparent_on);
        transparent_off = AnimationUtils.loadAnimation(ct, R.anim.transparent_off);
        flip_in = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_in);
        flip_out = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_out);
        past_flip_in = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_in);
        past_flip_out = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_out);

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
                startActivity(new Intent(ct, Setting.class));
            }
        });

        // 일일 암기표
        GridLayoutManager manager = new GridLayoutManager(ct, 2);
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
        adapter.setOnItemClickListener(new OnDailyTableClickListener() {
            // 일반적으로 터치할 때
            @Override
            public void onItemClick(DailyTableAdapter.ViewHolder holder, View view) {
                // 해상도 적용하기
                holder.front_card.setCameraDistance(1000 * scale);
                holder.back_card.setCameraDistance(1000 * scale);

                // 이전에 누른 뷰홀더가 지금 누른 뷰홀더와 같다면
                if (holder != prior_holder) {
                    // 이전의 누른 것이 있다면 -> 처음 누른 것이 아니라면
                    if (prior_holder != null) {
                        // 이전 뷰홀더는 앞면을 보여줌
                        past_flip_in.setTarget(prior_holder.back_card);
                        past_flip_out.setTarget(prior_holder.front_card);
                        past_flip_in.start();
                        past_flip_out.start();
                    }
                    // 현재 뷰홀더는 뒷면을 보여줌.
                    flip_in.setTarget(holder.front_card);
                    flip_out.setTarget(holder.back_card);
                    flip_in.start();
                    flip_out.start();
                    prior_holder = holder;
                }
            }
        });

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
