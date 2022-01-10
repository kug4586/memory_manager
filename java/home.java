package com.example.memory_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class home extends AppCompatActivity {
    boolean is_menu_open = false;
    boolean is_category_open = false;

    Animation translateLeftAnime;
    Animation translateRightAnime;
    Animation transparentOnAnime;
    Animation transparentOffAnime;

    LinearLayout menu_screen;
    LinearLayout category_screen;

    ImageButton menu_button;
    TextView classify_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 화면 객체
        menu_screen = findViewById(R.id.menu_screen);
        category_screen = findViewById(R.id.category_screen);

        // 애니메이션
        translateLeftAnime = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnime = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        transparentOnAnime = AnimationUtils.loadAnimation(this,R.anim.transparent_on);
        transparentOffAnime = AnimationUtils.loadAnimation(this, R.anim.transparent_off);

        // 애니메이션 리스너
        sliding_page_animation_listener sliding_animListener = new sliding_page_animation_listener();
        translateLeftAnime.setAnimationListener(sliding_animListener);
        translateRightAnime.setAnimationListener(sliding_animListener);
        appearing_page_animation_listener appearing_animListener = new appearing_page_animation_listener();
        transparentOnAnime.setAnimationListener(appearing_animListener);
        transparentOffAnime.setAnimationListener(appearing_animListener);


        // 메뉴 버튼
        menu_button = findViewById(R.id.menu_button);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_menu_open) {
                    menu_screen.startAnimation(translateLeftAnime);
                } else {
                    menu_screen.setVisibility(View.VISIBLE);
                    menu_screen.startAnimation(translateRightAnime);
                }
            }
        });

        // 카테고리 생성 버튼
        classify_button = findViewById(R.id.classify_button);
        classify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_category_open) {
                    category_screen.startAnimation(transparentOffAnime);
                } else {
                    category_screen.setVisibility(View.VISIBLE);
                    category_screen.startAnimation(transparentOnAnime);
                }
            }
        });
    }

    private class sliding_page_animation_listener implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            if (is_menu_open) {
                menu_screen.setVisibility(View.INVISIBLE);

                is_menu_open = false;
            } else {
                is_menu_open = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }


    private class appearing_page_animation_listener implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            if (is_category_open) {
                category_screen.setVisibility(View.INVISIBLE);
                is_category_open = false;
            } else {
                is_category_open = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }
}