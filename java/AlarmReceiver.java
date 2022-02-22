package com.example.android_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    final int SOUND = 1000410;
    final int VIBRATION = 1000202;
    final int SOUND_VIBRATION = 1000378;
    static String INTENT_DATA ="alarm";
    static private String channel_id = "alarm_channel";

    PendingIntent bus_route_pending_intent;
    DatabaseHelper db_helper;
    TaskStackBuilder stack_builder;

    @Override
    public void onReceive(Context context, Intent intent) {

        // MainActivity 클래스와 연결하는 인텐트
        Intent bus_route_intent = new Intent(context, MainActivity.class);

        // 앱 밖에서도 앱에 접근 할 수 있도록 설정
        stack_builder = TaskStackBuilder.create(context);
        stack_builder.addNextIntentWithParentStack(bus_route_intent);

        db_helper = new DatabaseHelper(context);

        // 인텐트에 담긴 값에 따라 실행
        switch (intent.getStringExtra(INTENT_DATA)) {
            case "review": ReviewAlarm(context); break;
            case "reset": ResetAlarm(context); break;
        }
    }

    // 초기화 알림
    public void ResetAlarm(Context context) {
        // 신호를 통해 PendingIntent를 Setting에서 가져옴
        bus_route_pending_intent = stack_builder.getPendingIntent(
                200, PendingIntent.FLAG_UPDATE_CURRENT
        );
        // DB의 상태 초기화
        db_helper.ResetReview();
        // 상단 메세지 생성
        final NotificationCompat.Builder notification_builder =
                new NotificationCompat.Builder(context, channel_id)
                        .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
                        .setDefaults(Notification.DEFAULT_VIBRATE) // 알림 방식식
                        .setAutoCancel(true) // 자동으로 취소됨
                        .setContentTitle("from. 메모리 매니저") // 제목
                        .setContentText("일일 복습표를 초기화 했어요!") // 내용
                        .setContentIntent(bus_route_pending_intent); // 인텐트

        // 알림 매니저
        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 오레오 버전 이후론 체널 설정이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channel_id, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        // 현재 시각에 맞춰 알림을 띄움
        int id = (int) System.currentTimeMillis();
        manager.notify(id, notification_builder.build());
    }

    // 복습 알림
    public void ReviewAlarm(Context context) {
        // 신호를 통해 PendingIntent를 Setting에서 가져옴
        bus_route_pending_intent = stack_builder.getPendingIntent(
                100, PendingIntent.FLAG_UPDATE_CURRENT
        );
        // 오늘 복습하지 않은 것들이 있다면
        if (db_helper.IsNotFinished()) {
            // 상단 메세지 생성
            final NotificationCompat.Builder notification_builder =
                    new NotificationCompat.Builder(context, channel_id)
                            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
                            .setAutoCancel(true) // 자동으로 취소됨
                            .setContentTitle("from. 메모리 매니저") // 제목
                            .setContentText("아직 오늘 복습할 것들이 남아있어요!") // 내용
                            .setContentIntent(bus_route_pending_intent); // 인텐트

            // 알림 방식
            SharedPreferences pref = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
            switch (pref.getInt("way", 0)) {
                case SOUND: notification_builder.setDefaults(Notification.DEFAULT_SOUND); break;
                case VIBRATION: notification_builder.setDefaults(Notification.DEFAULT_VIBRATE); break;
                case SOUND_VIBRATION: notification_builder.setDefaults(Notification.DEFAULT_ALL); break;
                default: notification_builder.setDefaults(Notification.DEFAULT_LIGHTS);
            }

            // 알림 매니저
            final NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // 오레오 버전 이후론 체널 설정이 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channel_id, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT
                );
                manager.createNotificationChannel(channel);
            }
            // 현재 시각에 맞춰 알림을 띄움
            int id = (int) System.currentTimeMillis();
            manager.notify(id, notification_builder.build());
        }
    }
}
