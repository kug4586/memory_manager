package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateTable extends AppCompatActivity {

    private String[] categories;
    int review_count;
    int type_of_setting_date;
    private String category;

    TextView create_table_cancel;
    TextView create_table_complete;
    EditText table_name;
    EditText enter_date_self;
    Spinner category_T;
    RadioGroup count_of_repeat;
    RadioGroup set_date;
    LinearLayout empty_space;

    DatabaseHelper db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        review_count = 0; // 반복 횟수 초기화
        type_of_setting_date = 0; // 날짜 설정 방식 초기화
        db_helper = new DatabaseHelper(this);

        // id로 뷰 객체 불러오기
        create_table_cancel = findViewById(R.id.create_table_cancel);
        create_table_complete = findViewById(R.id.create_table_complete);
        table_name = findViewById(R.id.table_name);
        enter_date_self = findViewById(R.id.enter_date_self);
        category_T = findViewById(R.id.category_T);
        count_of_repeat = findViewById(R.id.count_of_repeat);
        set_date = findViewById(R.id.set_date);
        empty_space = findViewById(R.id.EmptySpace);


        // 빈 공간 터치했을 때
        empty_space.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 키보드 열려있으면 닫기
                CloseKeyBoard(table_name);
                return true;
            }
        });


        // 카테고리 설정
        categories = db_helper.InquiryCategory();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_T.setAdapter(adapter);
        // 아이템 클릭했을 때
        category_T.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                category = categories[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        category_T.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 키보드 열려있으면 닫기
                CloseKeyBoard(table_name);
                return false;
            }
        });


        // <표 생성> 취소
        create_table_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // <표 생성> 완료
        create_table_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dates = new String[10];
                try {
                    dates = InflateDate(review_count, type_of_setting_date);
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "입력 방식에 맞춰 입력해주세요", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                if (dates[0] != null) {
                    Log.d("DB", "테이블 만듦 : " + table_name.getText().toString() + " / " + category + "\n"
                            + dates[0] + dates[1] + dates[2] + dates[3] + dates[4] + dates[5] + dates[6] + dates[7] + dates[8] + dates[9]);
                    db_helper.CreateReviewTable(table_name.getText().toString(), category, dates);
                    CloseKeyBoard(table_name);
                    finish();
                }
            }
        });


        // 아이템 클릭했을 때
        count_of_repeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // 키보드 열려있으면 닫기
                CloseKeyBoard(table_name);
                // 반복 횟수 설정
                if (i == R.id.repeat_5) {
                    review_count = 5;
                } else if (i == R.id.repeat_7) {
                    review_count = 7;
                } else if (i == R.id.repeat_10){
                    review_count = 10;
                }
            }
        });
        // 객체 클릭했을 때
        count_of_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 키보드 열려있으면 닫기
                CloseKeyBoard(table_name);
            }
        });


        // 날짜 설정
        // 선택 안됨
        enter_date_self.setEnabled(false);
        type_of_setting_date = 0;
        set_date.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.date_now) {
                    // 오늘 날짜
                    enter_date_self.setEnabled(false);
                    type_of_setting_date = 1;
                } else if (i == R.id.date_custom) {
                    // 사용자 지정 날짜
                    enter_date_self.setEnabled(true);
                    type_of_setting_date = 2;
                }
            }
        });
    }

    // 뒤로가기 버튼을 눌렀을 때
    @Override
    public void onBackPressed() {
        finish();
    }

    // 키보드 열린거 닫기
    public void CloseKeyBoard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive(et)) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    // 첫쌔날을 기준으로 암기 날짜 생성하기
    public String[] InflateDate(int count, int type) throws ParseException {
        // 첫째날 정하기
        Date first_day;
        if (type == 1) {
            first_day = new Date();
        } else if (type == 2) {
            SimpleDateFormat de_date_format = new SimpleDateFormat("yyyyMMdd");
            first_day = de_date_format.parse(enter_date_self.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
            return null;
        }

        // 계산하기
        Date[] dates_raw = new Date[10];
        long additional_date = 1000 * 60 * 60 * 24;

        dates_raw[0] = first_day;
        dates_raw[1] = new Date(first_day.getTime() + additional_date);
        dates_raw[2] = new Date(first_day.getTime() + additional_date*2);
        dates_raw[3] = new Date(first_day.getTime() + additional_date*5);
        dates_raw[4] = new Date(first_day.getTime() + additional_date*12);
        if (count > 5) {
            dates_raw[5] = new Date(first_day.getTime() + additional_date*22);
            dates_raw[6] = new Date(first_day.getTime() + additional_date*36);
            if (count > 7) {
                dates_raw[7] = new Date(first_day.getTime() + additional_date*57);
                dates_raw[8] = new Date(first_day.getTime() + additional_date*87);
                dates_raw[9] = new Date(first_day.getTime() + additional_date*147);
            }
        }

        // 날짜를 문자로 바꾸기
        String[] dates_text = new String[10];
        SimpleDateFormat date_format = new SimpleDateFormat("MM/dd");
        for (int i=0; i<10; i++) {
            if (dates_raw[i] != null) {
                dates_text[i] = date_format.format(dates_raw[i]);
            } else {
                dates_text[i] = null;
            }
        }

        Log.d("DB", "InflateDate 완료");
        return dates_text;
    }
}