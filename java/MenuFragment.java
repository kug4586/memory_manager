package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuFragment extends Fragment {

    public static interface MenuCallback {
        public void change_menu_statement();
        public void disappear_hahaha();
    }
    public MenuCallback callback;

    public boolean is_menu_open = false;
    private Context ct;
    private String[] categories;
    private int[][] color_count;

    BriefInfoAdapter adapter;

    Animation translate_left_anim;
    Animation translate_right_anim;

    LinearLayout menu_screen;
    View empty_space;
    ImageButton create_table_button;
    TextView classify_manager_button;
    RecyclerView brief_list;

    DatabaseHelper db_helper;

    // 프레그먼트와 액티비티가 연결될 때
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ct = context.getApplicationContext();
        if (context instanceof MenuCallback) {
            callback = (MenuCallback) context;
        }
        db_helper = new DatabaseHelper(context);
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
        brief_list = rootView.findViewById(R.id.brief_list);

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
                // 메뉴 화면 넣기
                if (is_menu_open) {
                    ChangeMenu();
                    callback.disappear_hahaha();
                    // 액티비티 이동하기
                    startActivity(new Intent(ct, CreateTable.class));
                }
            }
        });

        // <표 목록> 열기
        classify_manager_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_menu_open) {
                    // 액티비티 이동하기
                    startActivity(new Intent(ct, TableList.class));
                }
            }
        });

        // 간단한 목록
        LinearLayoutManager manager = new LinearLayoutManager(ct, RecyclerView.VERTICAL, false);
        brief_list.setLayoutManager(manager);
        adapter = new BriefInfoAdapter();

        adapter.SetOnItemClickListener(new OnBriefInfoClickListener() {
            @Override
            public void OnItemClick(BriefInfoAdapter.ViewHolder holder, View view, int position) {
                Intent intent = new Intent(ct, TableList.class);
                intent.putExtra("category", categories[position]);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        SetList();
        super.onResume();
    }

    // 테이블 설정하기
    public void SetList() {
        categories = db_helper.InquiryCategory();
        color_count = db_helper.InquiryColorAndCount();

        if (adapter.items.size() != 0) {
            adapter.RemoveAllItem();
        }
        for (int i=0; i<categories.length; i++) {
            adapter.AddItem(new BriefInfo(
                    categories[i] + " (" + color_count[0][i] + ")", color_count[1][i]-1));
        }
        brief_list.setAdapter(adapter);
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
