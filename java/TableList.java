package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableList extends AppCompatActivity {
    boolean is_category_open = false;

    Animation transparent_on;
    Animation transparent_off;

    LinearLayout create_category_screen;

    ImageButton add_category;
    TextView create_category_cancel;
    TextView create_category_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        // id로 뷰 객체 불러오기
        add_category = findViewById(R.id.add_category);
        create_category_screen = findViewById(R.id.create_category_screen);
        create_category_cancel = findViewById(R.id.create_category_cancel);
        create_category_complete = findViewById(R.id.create_category_complete);

        // 애니메이션
        transparent_on = AnimationUtils.loadAnimation(this, R.anim.transparent_on);
        transparent_off = AnimationUtils.loadAnimation(this, R.anim.transparent_off);

        // 애니메이션 리스너
        becoming_transparent_anime_listener appearing_anim_listener = new becoming_transparent_anime_listener();
        transparent_on.setAnimationListener(appearing_anim_listener);
        transparent_off.setAnimationListener(appearing_anim_listener);

        // 카테고리 추가
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCategoryScreen();
            }
        });

        // 카테고리 창이 열렸을 때
        create_category_screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 카테고리 만들기 취소
        create_category_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCategoryScreen();
            }
        });

        // 카테고리 만들기 완료
        create_category_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCategoryScreen();
            }
        });
    }

    // 뒤로 가기 버튼을 눌렀을 때
    @Override
    public void onBackPressed() {
        if (is_category_open) {
            ChangeCategoryScreen();
        } else {
            finish();
        }
    }

    private class becoming_transparent_anime_listener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationEnd(Animation animation) {
            if (is_category_open) {
                create_category_screen.setVisibility(View.GONE);
                is_category_open = false;
            } else {
                is_category_open = true;
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    public void ChangeCategoryScreen() {
        if (is_category_open) {
            create_category_screen.startAnimation(transparent_off);
        } else {
            create_category_screen.setVisibility(View.VISIBLE);
            create_category_screen.startAnimation(transparent_on);
        }
    }
}