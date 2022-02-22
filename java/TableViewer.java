package com.example.android_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class TableViewer extends AppCompatActivity {
    private String table_name;
    private String table_category;

    DatabaseHelper db_helper;

    ViewerAdapter viewer_adapter;

    TextView name;
    TextView category;
    ImageButton delete_table;
    RecyclerView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_viewer);
        db_helper = new DatabaseHelper(this);

        // id로 객체 불러오기
        name = findViewById(R.id.name);
        category = findViewById(R.id.category);
        delete_table = findViewById(R.id.delete_table);
        review = findViewById(R.id.review);

        // 인텐트 처리
        ProcessIntent();

        // 레이아웃 설정
        SettingData();

        // 테이블 삭제
        delete_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 마지막 경고창
                AlertDialog.Builder alarm = new AlertDialog.Builder(TableViewer.this)
                        .setTitle("주의")
                        .setMessage("정말 삭제하시겠습니까?\n되돌릴 수 없어요!")
                        .setNegativeButton("아니요", null)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 삭제
                                db_helper.DeleteReviewTable(table_name, table_category);
                                // 알림창
                                AlertDialog.Builder dlg = new AlertDialog.Builder(TableViewer.this)
                                        .setMessage(table_name + "이(가) 삭제되었습니다")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // 창 닫기
                                                finish();
                                            }
                                        });
                                dlg.show();
                            }
                        });
                alarm.show();
            }
        });
    }

    // 인텐트 처리하기
    private void ProcessIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            table_name = intent.getStringExtra("name");
            table_category = intent.getStringExtra("category");
        }
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
        // 복습했다고 체크하기
        viewer_adapter.SetOnCheckListener(new OnCheckListener() {
            @Override
            public void OnCheck(ViewerAdapter.ViewHolder holder, View view, int position) {
                db_helper.RecordReview(table_name, table_category);
                holder.Checking(view);
            }
        });
        // 날짜
        for (int i=1; i<data.length; i++) {
            // 데이터에 '/'가 있으면, 날짜임
            if (data[i].contains("/")) {
                // i번째 날이 복습한 날짜보다 이전이라면
                if (i<Integer.parseInt(data[0])) {
                    // 체크된 상태로 출력
                    viewer_adapter.AddItem(new Viewer(data[i], true));
                } else {
                    viewer_adapter.AddItem(new Viewer(data[i], false));
                }
            } else {
                break;  // 날짜를 순차적으로 입력했기 때문에 필요한 만큼만 반복
            }
        }

        review.setAdapter(viewer_adapter);
    }
}