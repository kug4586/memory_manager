package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Setting extends AppCompatActivity {

    boolean use = false;
    int HH,MM,way;
    String str;

    TextView setting_cancel;
    TextView setting_complete;
    CheckBox alarm_on_off;
    TextView set_time;
    RadioGroup choice_method;
    View no_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // id로 뷰 객체 불러오기
        setting_cancel = findViewById(R.id.setting_cancel);
        setting_complete = findViewById(R.id.setting_complete);
        alarm_on_off = findViewById(R.id.alarm_on_off);
        set_time = findViewById(R.id.set_time);
        choice_method = findViewById(R.id.ChoiceMethod);

        // 환경 설정에서 기존 설정 상태 가져오기
        SharedPreferences pref = getSharedPreferences("alarm", MODE_PRIVATE);
        boolean on_off = pref.getBoolean("OnOff", false);
        // 알람을 설정해 뒀다면
        if (on_off) {
            // 알람 체크 박스 선택됨으로 설정
            alarm_on_off.setChecked(true);
            // 문자 색 변경
            ClickManager(true);
            // 시간 설정
            long time = pref.getLong("time", Calendar.getInstance().getTimeInMillis());
            String alter = new SimpleDateFormat("a").format(time);
            if (alter == "오전") {
                set_time.setText("AM  " + new SimpleDateFormat("hh : mm").format(time));
            } else {
                set_time.setText("PM  " + new SimpleDateFormat("hh : mm").format(time));
            }
            // 알림 방식 설정
            int method = pref.getInt("way", 0);
            choice_method.check(method);
            // 변수 설정
            use = true;
            HH = Integer.parseInt(new SimpleDateFormat("HH").format(time));
            MM = Integer.parseInt(new SimpleDateFormat("mm").format(time));
            way = method;
        } else {
            // 알람 체크 박스 해제됨으로 설정
            alarm_on_off.setChecked(false);
            // 문자 색 변경
            ClickManager(false);
        }

        // 설정 취소
        setting_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 설정 완료
        setting_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetAlarm(use);
                finish();
            }
        });

        // 알림 버튼 꺼져있을 때 설정 변경 방지
        no_setting = findViewById(R.id.NoSetting);
        no_setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 알람 온오프 버튼
        alarm_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarm_on_off.isChecked()) {
                    ClickManager(true);
                    use = true;
                } else {
                    ClickManager(false);
                    use = false;
                }
            }
        });

        // 알림 시간 변경
        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(
                        Setting.this, // 시작할 액티비티
                        AlertDialog.THEME_HOLO_LIGHT, // 대화 상자의 테마
                        TimeSetListener, // 리스너
                        HH, MM, // 적용할 시각
                        false // false : 오전&오후로 표기 / true : 24시로 표기
                ).show();
            }
        });

        // 알림 방식 변경
        choice_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.sound) {
                    way = R.id.sound;
                } else if (i == R.id.vibration) {
                    way = R.id.vibration;
                } else if (i == R.id.sound_and_vibration) {
                    way = R.id.sound_and_vibration;
                }
            }
        });
    }

    // 뒤로가기 버튼을 눌렀을 때
    @Override
    public void onBackPressed() {
        finish();
    }

    // 알람 설정
    public void SetAlarm(boolean want) {
        // 지정한 시간으로 알람 시간을 설정하기
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, HH);
        calendar.set(Calendar.MINUTE, MM);
        calendar.set(Calendar.SECOND, 0);
        // 만약 시간이 설정했던 때를 지났다면, 다음날 같은 시간으로 다시 한번 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        // 알람 설정
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 알람 매니저가 존재하면
        if (manager != null) {
            // 사전 준비
            PackageManager pm = this.getPackageManager();
            ComponentName receiver = new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            // 만약 알람을 설정할 거라면
            if (want) {
                // 인텐트에 데이터 담기
                intent.putExtra("alarm", "review");
                // pending : ~가 있을 때까지, 미결의, 곧 있을 임박한
                PendingIntent alarm_intent = PendingIntent.getBroadcast(
                        getApplicationContext(), // 실행 위치
                        100, // 신호
                        intent, // 인텐트
                        PendingIntent.FLAG_UPDATE_CURRENT // '업데이트' 전달
                );
                // 반복되는 알람을 설정
                manager.setRepeating(
                        manager.RTC_WAKEUP,         // 지정한 시간에 기기의 절전 모드를 해제하여 인텐트를 실행
                        calendar.getTimeInMillis(), // 설정한 시간
                        AlarmManager.INTERVAL_DAY,  // 반복 간격
                        alarm_intent                // 인텐트
                );
                // 핸드폰 재시작에 실행되는 리시버 사용 가능하게 설정
                pm.setComponentEnabledSetting(
                        receiver, // 보내는 위치
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, // 컴포넌트를 이용 가능하게 (?)
                        PackageManager.DONT_KILL_APP // 앱 죽이지 않기 (?)
                );
                // 환경 변수에 저장
                SharedPreferences.Editor editor = getSharedPreferences("alarm", MODE_PRIVATE).edit();
                editor.putBoolean("OnOff", true);
                editor.putInt("way", way);
                editor.putLong("time", calendar.getTimeInMillis());
                editor.apply();
                // 공지
                Toast.makeText(getApplicationContext(), str + "에 알람이 울립니다", Toast.LENGTH_SHORT).show();
            } else { // 해제할 거라면
                PendingIntent cancel_intent = PendingIntent.getBroadcast(
                        getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                // 알람 취소
                manager.cancel(cancel_intent);
                // 핸드폰 재시작에 실행되는 리시버 사용 못하게 설정
                pm.setComponentEnabledSetting(
                        receiver, // 보내는 위치
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, // 컴포넌트를 이용 가능하게 (?)
                        PackageManager.DONT_KILL_APP // 앱 죽이지 않기 (?)
                );
                // 환경 변수에 저장
                SharedPreferences.Editor editor = getSharedPreferences("alarm", MODE_PRIVATE).edit();
                editor.putBoolean("OnOff", false);
                editor.apply();
                // 공지
                Toast.makeText(getApplicationContext(), "알람을 해제합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 시각 대화상자 생성 리스너
    TimePickerDialog.OnTimeSetListener TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            // 데이터 저장
            HH = hour;
            MM = minute;
            // 표기법 설정
            if (hour < 1) {
                str += "AM  12 : ";
            } else if (hour < 10) {
                str += "AM  0" + hour + " : ";
            } else if (hour < 12) {
                str += "AM  " + hour + " : ";
            } else if (hour < 13) {
                str += "PM  12 : ";
            } else if (hour < 22){
                str += "PM  0" + (hour-12) + " : ";
            } else {
                str += "PM  " + (hour-12) + " : ";
            }
            if (minute < 10) {
                str += "0" + minute;
            } else {
                str += minute;
            }
            // 화면에 표시하기
            set_time.setText(str);
        }
    };

    // 알림 설정 기능의 클릭을 막기
    @SuppressLint("NewApi")
    public void ClickManager(boolean clickable) {
        View no_setting = findViewById(R.id.NoSetting);
        TextView alarm_time = findViewById(R.id.alarm_time);
        TextView alarm_method = findViewById(R.id.alarm_method);
        RadioButton sound = findViewById(R.id.sound);
        RadioButton vibration = findViewById(R.id.vibration);
        RadioButton SnV = findViewById(R.id.sound_and_vibration);

        if (clickable) {
            no_setting.setVisibility(View.GONE);
            alarm_time.setTextColor(getColor(R.color.black));
            alarm_method.setTextColor(getColor(R.color.black));
            sound.setTextColor(getColor(R.color.black));
            vibration.setTextColor(getColor(R.color.black));
            SnV.setTextColor(getColor(R.color.black));
            set_time.setTextColor(getColor(R.color.black));
        } else {
            no_setting.setVisibility(View.VISIBLE);
            alarm_time.setTextColor(getColor(R.color.gray));
            alarm_method.setTextColor(getColor(R.color.gray));
            sound.setTextColor(getColor(R.color.gray));
            vibration.setTextColor(getColor(R.color.gray));
            SnV.setTextColor(getColor(R.color.gray));
            set_time.setTextColor(getColor(R.color.gray));
        }
    }
}