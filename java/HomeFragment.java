package com.example.android_app;

import android.animation.Animator;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HomeFragment extends Fragment {

    // 인터페이스 : fragment의 정보를 Activity로
    public static interface HomeCallback {
        public void change_menu();
    }
    public HomeCallback callback;

    public boolean is_menu_close = true;
    private Context ct;

    SimpleDateFormat format = new SimpleDateFormat("MM/dd");
    public Date today;
    public String today_text;

    Animation transparent_on;
    Animation transparent_off;
    AnimatorSet flip_in;
    AnimatorSet flip_out;
    AnimatorSet prior_flip_in;
    AnimatorSet prior_flip_out;

    DailyTableAdapter adapter;
    DailyTableAdapter.ViewHolder prior_holder;

    ImageView menu_button;
    ImageView setting_button;
    RecyclerView daily_tables_screen;
    View hahaha;
    View no_touch;

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
        today = new Date();
        today_text = format.format(today);
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
        no_touch = rootView.findViewById(R.id.NoTouch);

        // 해상도 값 가져오기
        Float scale = ct.getResources().getDisplayMetrics().density;

        // 투명화 애니메이션
        transparent_on = AnimationUtils.loadAnimation(ct, R.anim.transparent_on);
        transparent_off = AnimationUtils.loadAnimation(ct, R.anim.transparent_off);

        anim_listener listener = new anim_listener();
        transparent_on.setAnimationListener(listener);
        transparent_off.setAnimationListener(listener);

        // 카드 플립 애니메이션
        flip_in = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_in);
        flip_out = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_out);
        prior_flip_in = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_in);
        prior_flip_out = (AnimatorSet) AnimatorInflater.loadAnimator(ct, R.anim.flip_out);

        // 메뉴가 열려있을 때, 홈 화면 클릭을 막음
        hahaha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 리싸이클러뷰 애니메이션 동작할 때 터치 막기
        no_touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 메뉴 버튼
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메뉴 화면 넣기
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
                // 액티비티 이동
                startActivity(new Intent(ct, Setting.class));
            }
        });

        // 일일 암기표
        GridLayoutManager manager = new GridLayoutManager(ct, 2);
        daily_tables_screen.setLayoutManager(manager);
        adapter = new DailyTableAdapter();

        adapter.setOnItemClickListener(new OnDailyTableClickListener() {
            // 일반적으로 터치할 때
            @Override
            public void onItemClick(DailyTableAdapter.ViewHolder holder, View view) {
                // 터치 방지 활성화
                no_touch.setVisibility(View.VISIBLE);

                // 해상도 적용하기
                holder.front_card.setCameraDistance(1000 * scale);
                holder.back_card.setCameraDistance(1000 * scale);

                // 애니메이션에 리스너 설정
                flip_in.removeAllListeners();
                prior_flip_in.removeAllListeners();
                flip_in.addListener(new CardFlipListener(holder));
                prior_flip_in.addListener(new CardFlipListener(prior_holder));

                // 이전에 누른 뷰홀더가 지금 누른 뷰홀더와 다르다면
                if (holder != prior_holder) {
                    // 이전의 누른 것이 있다면 -> 처음 누른 것이 아니라면
                    if (prior_holder != null) {
                        // 이전 뷰홀더는 앞면을 보여줌
                        prior_flip_in.setTarget(prior_holder.back_card);
                        prior_flip_out.setTarget(prior_holder.front_card);
                        prior_flip_in.start();
                        prior_flip_out.start();
                    }
                    // 현재 뷰홀더는 뒷면을 보여줌.
                    flip_in.setTarget(holder.front_card);
                    flip_out.setTarget(holder.back_card);
                    flip_in.start();
                    flip_out.start();
                    prior_holder = holder;
                }

                // 터치 방지 해제
                no_touch.setVisibility(View.GONE);
            }
            // 복습 버튼을 꾹 눌렀을 때
            @Override
            public void OnLongClick(DailyTableAdapter.ViewHolder holder, View view, int position) {
                // 터치 방지 활성화
                no_touch.setVisibility(View.VISIBLE);

                // 실행된 아이템 설정하기
                DailyTable item = adapter.getItem(position);
                // DB에 기록하기
                db_helper.RecordReview(item.get_table_name(), item.GetCategory());
                // 애니메이션
                Animation review_anim = AnimationUtils.loadAnimation(ct, R.anim.check);
                Animation disappear_anim = AnimationUtils.loadAnimation(ct, R.anim.disappear);

                // 복습 체크 애니메이션 리스너
                review_anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                holder.itemView.startAnimation(disappear_anim);
                            }
                        }, 1000); // 1초 뒤에 실행
                   }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                // 사라짐 애니메이션 리스너
                disappear_anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.itemView.setVisibility(View.GONE);
                        // 일일복습표 설정하기
                        adapter.RemoveItem(position);
                        // 터치 방지 해제
                        no_touch.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                holder.review_img.setVisibility(View.VISIBLE);
                holder.review_img.startAnimation(review_anim);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        SetDailyTablesScreen();
        super.onResume();
    }

    // 일일복습표 설정하기
    public void SetDailyTablesScreen() {
        // db에서 데이터 뽑기
        String[][] daily_table_group = db_helper.FilterDailyTable(today_text);
        // 어뎁터의 기존 데이터 삭제
        if (adapter.items.size() != 0) {
            adapter.RemoveAllItem();
        }
        // 어뎁터에 아이템 추가
        for (int i=0; i<daily_table_group.length; i++) {
            adapter.addItem(new DailyTable(
                    daily_table_group[i][0], daily_table_group[i][1], daily_table_group[i][2]));
        }
        // 리싸이클러뷰 설정하기
        daily_tables_screen.setAdapter(adapter);
    }

    // 카드 플립 애니메이션 리스너
    private class CardFlipListener implements Animator.AnimatorListener {
        DailyTableAdapter.ViewHolder holder;
        public CardFlipListener(DailyTableAdapter.ViewHolder holder) {
            this.holder = holder;
        }
        @Override
        public void onAnimationStart(Animator animator) {
            if (holder.is_flipped) {
                holder.front_card.setVisibility(View.VISIBLE);
            } else {
                holder.back_card.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            if (holder.is_flipped) {
                holder.back_card.setVisibility(View.GONE);
                holder.is_flipped = false;
            } else {
                holder.front_card.setVisibility(View.GONE);
                holder.is_flipped = true;
            }
        }
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}
    }

    // 터치 방지용 뷰의 애니메이션 리스너
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

    // 버튼 온오프
    public void button_on_off() {
        if (is_menu_close) {
            menu_button.setEnabled(false);
            is_menu_close = false;
        } else {
            menu_button.setEnabled(true);
            is_menu_close = true;
        }
    }

    // 터치 방지용 뷰의 애니메이션
    public void set_hahaha() {
        if (is_menu_close) {
            hahaha.setVisibility(View.VISIBLE);
            hahaha.startAnimation(transparent_on);
        } else {
            hahaha.startAnimation(transparent_off);
        }
    }
}
