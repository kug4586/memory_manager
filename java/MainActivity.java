package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

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

        // 만약 처음 실행한 거라면
        SharedPreferences pref = getSharedPreferences("FirstRun", MODE_PRIVATE);
        if (pref != null && pref.getBoolean("IsFirst", true)) {
            // 복습 초기화 알람 설정해놓기
            ResetAlarm();
            // 처음이 아니라고 기록하기
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("IsFirst", false);
            editor.commit();
        }
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

    // 복습 초기화 설정하기
    public void ResetAlarm() {
        // 알람 시간을 자정으로 설정하기
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 만약 시간이 설정했던 때를 지났다면, 다음날 같은 시간으로 다시 한번 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        // 알람 설정
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmManager.class);
        intent.putExtra("alarm", "reset");
        PendingIntent alarm_intent = PendingIntent.getBroadcast(
                getApplicationContext(), // 실행 위치
                200, // 신호
                intent, // 인텐트
                PendingIntent.FLAG_UPDATE_CURRENT // '업데이트' 전달
        );
        manager.setRepeating(
                manager.RTC_WAKEUP,         // 지정한 시간에 기기의 절전 모드를 해제하여 인텐트를 실행
                calendar.getTimeInMillis(), // 설정한 시간
                AlarmManager.INTERVAL_DAY,  // 반복 간격
                alarm_intent                // 인텐트
        );

        // 핸드폰 재시작에 실행되는 리시버 사용 가능하게 하기
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
        pm.setComponentEnabledSetting(
                receiver, // 보내는 위치
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, // 컴포넌트를 이용 가능하게 (?)
                PackageManager.DONT_KILL_APP // 앱 죽이지 않기 (?)
        );
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