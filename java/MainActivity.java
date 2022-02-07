package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeCallback, MenuFragment.MenuCallback {

    public boolean is_menu_open = false;
    int table_count;

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
        ResortedState();
        db_helper = new DatabaseHelper(this);
        db_helper.GetTableCount(table_count);
        db_helper.getWritableDatabase();
    }

    // 액티비티가 파괴될 때
    @Override
    protected void onDestroy() {
        // 데이터베이스 핼퍼로 연결 끊기
        table_count = db_helper.SetTableCount();
        SaveState();
        db_helper.close();
        super.onDestroy();
    }

    // 환경변수 복원하기
    protected void ResortedState() {
        SharedPreferences pref = getSharedPreferences("TableCount", Activity.MODE_PRIVATE);
        if (pref != null && pref.contains("TableCount")) {
            table_count = pref.getInt("TableCount", 0);
        }
    }

    // 환경변수 저장하기
    protected void SaveState() {
        SharedPreferences pref = getSharedPreferences("TableCount", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("TableCount", table_count);
        editor.commit();
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