package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TableViewer extends AppCompatActivity {
    private String table_name;
    private String table_category;

    DatabaseHelper db_helper;

    ViewerAdapter viewer_adapter;

    TextView name;
    TextView category;
    RecyclerView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_viewer);
        db_helper = new DatabaseHelper(this);

        // id로 객체 불러오기
        name = findViewById(R.id.name);
        category = findViewById(R.id.category);
        review = findViewById(R.id.review);

        // 인텐트 처리
        ProcessIntent();

        // 레이아웃 설정
        SettingData();
    }

    // 인텐트 처리하기
    private void ProcessIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            table_name = intent.getStringExtra("name");
            table_category = intent.getStringExtra("category");
        }
        Log.d("test", table_name + " / " + table_category);
    }

    // DB에서 데이터 뽑아서 레이아웃에 설정하기
    private void SettingData() {
        // 데이터 뽑기
        String[] data = db_helper.InquiryTable(table_name, table_category);

        // 이름과 카테고리
        name.setText(table_name);
        category.setText(table_category);

        // 날짜별 복습 확인표
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        review.setLayoutManager(manager);
        viewer_adapter = new ViewerAdapter();

        for (int i=1; i<data.length; i++) {
            if (data[i] != null) {
                viewer_adapter.AddItem(new Viewer(data[i]));
            } else {
                break;
            }
        }
        review.setAdapter(viewer_adapter);
    }
}