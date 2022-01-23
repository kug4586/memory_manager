package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeCallback, MenuFragment.MenuCallback {

    HomeFragment home_fragment;
    MenuFragment menu_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프레그먼트
        FragmentManager manager = getSupportFragmentManager();
        home_fragment = (HomeFragment) manager.findFragmentById(R.id.home);
        menu_fragment = (MenuFragment) manager.findFragmentById(R.id.menu);
    }

    @Override
    public void change_menu() {
        menu_fragment.ChangeMenu();
    }

    @Override
    public void change_menu_statement() {
        home_fragment.button_on_off();
    }

    @Override
    public void disappear_hahaha() {
        home_fragment.set_hahaha();
    }
}