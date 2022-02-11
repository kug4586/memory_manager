package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeCallback, MenuFragment.MenuCallback {

    public boolean is_menu_open = false;

    HomeFragment home_fragment;
    MenuFragment menu_fragment;

    DatabaseHelper db_helper;

    // 액티비티가 생성될 때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프레그먼트
        FragmentManager manager = getSupportFragmentManager();
        home_fragment = (HomeFragment) manager.findFragmentById(R.id.home);
        menu_fragment = (MenuFragment) manager.findFragmentById(R.id.menu);

        // 데이터베이스 핼퍼 부르기
        db_helper = new DatabaseHelper(this);
    }

    // 액티비티가 파괴될 때
    @Override
    protected void onDestroy() {
        db_helper.close();
        super.onDestroy();
    }

    // 뒤로가기 버튼을 눌렀을 때
    @Override
    public void onBackPressed() {
        if (is_menu_open) {
            menu_fragment.ChangeMenu();
            home_fragment.set_hahaha();
        } else {
            super.onBackPressed();
        }
    }

    // 인터페이스를 통한 매소드 실행
    @Override
    public void change_menu() {
        menu_fragment.ChangeMenu();
    }
    @Override
    public void change_menu_statement() {
        if (is_menu_open) {
            is_menu_open = false;
        } else {
            is_menu_open = true;
        }
        home_fragment.button_on_off();
    }
    @Override
    public void disappear_hahaha() {
        home_fragment.set_hahaha();
    }
}