package com.example.memory_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class home extends AppCompatActivity {
    boolean isPageOpen = false;

    Animation translateLeftAnime;
    Animation translateRightAnime;

    LinearLayout menu_screen;
    ImageButton menu_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menu_screen = findViewById(R.id.menu_screen);

        translateLeftAnime = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnime = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        sliding_page_animation_listener animListener = new sliding_page_animation_listener();
        translateLeftAnime.setAnimationListener(animListener);
        translateRightAnime.setAnimationListener(animListener);

        menu_button = findViewById(R.id.menu_button);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPageOpen) {
                    menu_screen.startAnimation(translateLeftAnime);
                } else {
                    menu_screen.setVisibility(View.VISIBLE);
                    menu_screen.startAnimation(translateRightAnime);
                }
            }
        });
    }

    private class sliding_page_animation_listener implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                menu_screen.setVisibility(View.INVISIBLE);

                isPageOpen = false;
            } else {
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }
}