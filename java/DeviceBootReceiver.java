package com.example.android_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // 환경 설정에서 알람이 설정된 상태라면, 복습 알람 설정하기
            SharedPreferences pref = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
            if (pref.getBoolean("OnOff", false)) {

                // 사전 준비
                Intent alarm_intent = new Intent(context, AlarmReceiver.class);
                PendingIntent pending_intent = PendingIntent.getBroadcast(
                        context,        // 실행 위치
                        100, // 신호
                        alarm_intent,  // 인텐트
                        0         // (?)
                );
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                intent.putExtra("alarm", "review");

                // 시간 설정
                Long time = pref.getLong("time", Calendar.getInstance().getTimeInMillis());
                Calendar current = Calendar.getInstance();
                Calendar next_time = new GregorianCalendar();
                next_time.setTimeInMillis(pref.getLong("time", time));
                if (current.after(next_time)) {
                    next_time.add(Calendar.DATE, 1);
                }

                // 알람 설정
                if (manager != null) {
                    manager.setRepeating(
                            manager.RTC_WAKEUP,          // 지정한 시간에 기기의 절전 모드를 해제하여 인텐트를 실행
                            next_time.getTimeInMillis(), // 설정한 시간
                            AlarmManager.INTERVAL_DAY,   // 반복 간격
                            pending_intent               // 인텐트
                    );
                }
            }

            // 초기화 알람 설정하기
            // 사전 준비
            Intent reset_intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pending_intent = PendingIntent.getBroadcast(
                    context,        // 실행 위치
                    200, // 신호
                    reset_intent,  // 인텐트
                    0         // (?)
            );
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent.putExtra("alarm", "reset");

            // 시간 설정
            Calendar current = Calendar.getInstance();
            Calendar next_time = new GregorianCalendar();
            next_time.setTimeInMillis(System.currentTimeMillis());
            next_time.set(Calendar.HOUR_OF_DAY, 0);
            if (current.after(next_time)) {
                next_time.add(Calendar.DATE, 1);
            }

            // 알람 설정
            if (manager != null) {
                manager.setRepeating(
                        manager.RTC_WAKEUP,          // 지정한 시간에 기기의 절전 모드를 해제하여 인텐트를 실행
                        next_time.getTimeInMillis(), // 설정한 시간
                        AlarmManager.INTERVAL_DAY,   // 반복 간격
                        pending_intent               // 인텐트
                );
            }
        }
    }
}
