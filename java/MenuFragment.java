package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {

    public static interface MenuCallback {
        public void change_menu_statement();
        public void disappear_hahaha();
    }
    public MenuCallback callback;

    public boolean is_menu_open = false;
    private Context ct;

    Animation translate_left_anim;
    Animation translate_right_anim;

    LinearLayout menu_screen;
    View empty_space;
    ImageButton create_table_button;
    TextView classify_manager_button;

    // 프레그먼트와 액티비티가 연결될 때
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ct = context.getApplicationContext();
        if (context instanceof MenuCallback) {
            callback = (MenuCallback) context;
        }
    }

    // 프레그먼트에 뷰를 전달함
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu, container, false);

        // id로 뷰 객체 불러오기
        menu_screen = rootView.findViewById(R.id.menu_screen);
        empty_space = rootView.findViewById(R.id.empty_space);
        create_table_button = rootView.findViewById(R.id.create_table_button);
        classify_manager_button = rootView.findViewById(R.id.classify_manager_button);

        // 애니메이션 불러오기
        translate_left_anim = AnimationUtils.loadAnimation(ct, R.anim.translate_left);
        translate_right_anim = AnimationUtils.loadAnimation(ct, R.anim.translate_right);

        // 애니메이션 리스너
        menu_anim_listener listener = new menu_anim_listener();
        translate_left_anim.setAnimationListener(listener);
        translate_right_anim.setAnimationListener(listener);

        // 메뉴 닫기
        empty_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_menu_open) {
                    ChangeMenu();
                    callback.disappear_hahaha();
                }
            }
        });

        // <표 생성> 열기
        create_table_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ct, CreateTable.class));
            }
        });

        // <표 목록> 열기
        classify_manager_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ct, TableList.class));
            }
        });

        return rootView;
    }

    // 메뉴 애니메이션 리스너
    private class menu_anim_listener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationEnd(Animation animation) {
            if (is_menu_open) {
                menu_screen.setVisibility(View.GONE);
                is_menu_open = false;
            } else {
                is_menu_open = true;
            }
            callback.change_menu_statement();
        }
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    // 메뉴 열고 닫기
    public void ChangeMenu() {
        if (is_menu_open) {
            menu_screen.startAnimation(translate_left_anim);
        } else {
            menu_screen.setVisibility(View.VISIBLE);
            menu_screen.startAnimation(translate_right_anim);
        }
    }
}
