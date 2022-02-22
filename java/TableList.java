package com.example.android_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TableList extends AppCompatActivity {
     public int images[][] = {
            {R.drawable.red, R.drawable.red_},
            {R.drawable.orange, R.drawable.orange_},
            {R.drawable.yellow, R.drawable.yellow_},
            {R.drawable.green, R.drawable.green_},
            {R.drawable.blue, R.drawable.blue_},
            {R.drawable.darkblue, R.drawable.darkblue_},
            {R.drawable.violet, R.drawable.violet_}
    };
    public boolean is_category_open = false;
    boolean is_first = true;
    public int color_num;
    public String[][] classified_tables;
    public String[] categories;
    public String category;

    DatabaseHelper db_helper;

    ReviewRecordAdapter table_adapter;

    Animation transparent_on;
    Animation transparent_off;

    LinearLayout create_category_screen;
    ImageButton add_category;
    ImageButton delete_category;
    TextView create_category_cancel;
    TextView create_category_complete;
    RecyclerView table_list;
    Spinner category_L;
    EditText category_name;
    ImageView red, orange, yellow, green, blue, darkblue, violet, selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
        db_helper = new DatabaseHelper(this);

        // id로 뷰 객체 불러오기
        add_category = findViewById(R.id.add_category);
        delete_category = findViewById(R.id.delete_category);
        create_category_screen = findViewById(R.id.create_category_screen);
        create_category_cancel = findViewById(R.id.create_category_cancel);
        create_category_complete = findViewById(R.id.create_category_complete);
        table_list = findViewById(R.id.table_list);
        category_L = findViewById(R.id.category_L);
        category_name = findViewById(R.id.category_name);
        red = findViewById(R.id.red);
        orange = findViewById(R.id.orange);
        yellow = findViewById(R.id.yellow);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        darkblue = findViewById(R.id.darkblue);
        violet = findViewById(R.id.violet);


        // 애니메이션
        transparent_on = AnimationUtils.loadAnimation(this, R.anim.transparent_on);
        transparent_off = AnimationUtils.loadAnimation(this, R.anim.transparent_off);
        // 애니메이션 리스너
        becoming_transparent_anime_listener appearing_anim_listener = new becoming_transparent_anime_listener();
        transparent_on.setAnimationListener(appearing_anim_listener);
        transparent_off.setAnimationListener(appearing_anim_listener);


        // 카테고리 추가
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정 초기화
                if (selected != null) {
                    selected.setImageResource(images[color_num][0]);
                }
                color_num = -1;
                selected = null;
                category_name.setText("");
                ChangeCategoryScreen();
            }
        });
        // 카테고리 창이 열렸을 때
        create_category_screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CloseKeyBoard(category_name);
                return true;
            }
        });
        // 색상 선택하기
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(0, red);
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(1, orange);
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(2, yellow);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(3, green);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(4, blue);
            }
        });
        darkblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(5, darkblue);
            }
        });
        violet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceColor(6, violet);
            }
        });
        // 카테고리 만들기 취소
        create_category_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCategoryScreen();
                CloseKeyBoard(category_name);
            }
        });
        // 카테고리 만들기 완료
        create_category_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = category_name.getText().toString();
                // 이름이 공백이 아니고 색상도 선택했다면
                if (category_name.length() != 0 && color_num > -1) {
                    // 이미 존재하는 카테고리라면
                    if (IsInArray(name)) {
                        Toast.makeText(getApplicationContext(), "이미 존재하는 카테고리입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        // DB에 저장하기
                        db_helper.CreateCategory(name, color_num);
                        // 창 닫기
                        SetSpinner();
                        ChangeCategoryScreen();
                        CloseKeyBoard(category_name);
                    }
                } else {
                    // 이름이 공백이고 색상을 선택하지 않았다면
                    if (category_name.length() == 0 && color_num < 0) {
                        Toast.makeText(getApplicationContext(), "정보가 입력되지 않았어요", Toast.LENGTH_SHORT).show();
                    // 색상을 선택하지 않았다면
                    } else if (color_num < 0) {
                        Toast.makeText(getApplicationContext(), "색상을 선택해 주세요", Toast.LENGTH_SHORT).show();
                    // 이름을 입력하지 않았다면
                    } else {
                        Toast.makeText(getApplicationContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // 카테고리 삭제하기
        delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 초기 데이터 설정
                String[] arr = new String[categories.length-1];
                boolean[] check = new boolean[categories.length-1];
                for (int i=0; i<arr.length; i++) {
                    arr[i] = categories[i+1];
                }
                for (int i=0; i<check.length; i++) {
                    check[i] = false;
                }
                // 대화 상자 띄우기
                AlertDialog.Builder dlg = new AlertDialog.Builder(TableList.this);
                dlg.setTitle("카테고리 삭제")
                        .setMultiChoiceItems(arr, check, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                check[i] = b;
                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean is_checked = false;
                                for (int n=0; n<check.length; n++) {
                                    if (check[n]) {
                                        is_checked = true;
                                        break;
                                    }
                                }
                                if (is_checked) {
                                    AlertDialog.Builder last = new AlertDialog.Builder(TableList.this)
                                            .setTitle("주의!")
                                            .setMessage("정말로 지우시겠습니까?")
                                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    for (int k=1; k<categories.length; k++) {
                                                        if (check[k-1]) {
                                                            db_helper.DeleteCategory(categories[k]);
                                                        }
                                                    }
                                                    SetSpinner();
                                                }
                                            })
                                            .setNegativeButton("아니요", null);
                                    last.show();
                                } else {
                                    AlertDialog.Builder error = new AlertDialog.Builder(TableList.this)
                                            .setMessage("선택된 카테고리가 없습니다")
                                            .setPositiveButton("확인", null);
                                    error.show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });


        // 카테고리 목록의 요소 클릭했을 때
        category_L.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (is_first && category != null) {
                    for (int i=0; i<categories.length; i++) {
                        if (categories[i].contains(category)) {
                            adapterView.setSelection(i);
                        }
                    }
                    is_first = false;
                } else {
                    category = categories[position];
                    // 리싸이클러뷰 설정하기
                    SetRecyclerView(null, category);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        // 테이블 목록
        LinearLayoutManager manager = new LinearLayoutManager(
                getApplicationContext(), RecyclerView.VERTICAL, false);
        table_list.setLayoutManager(manager);
        table_adapter = new ReviewRecordAdapter();
        // 클릭 리스너
        table_adapter.SetItemClickListener(new OnReviewRecordClickListener() {
            @Override
            public void OnItemClick(ReviewRecordAdapter.ViewHolder holder, View view, int position) {
                ReviewRecord item = table_adapter.GetItem(position);
                Intent intent = new Intent(getApplicationContext(), TableViewer.class);
                intent.putExtra("name", item.GetTableName());
                intent.putExtra("category", item.GetCategory());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        if (intent.getStringExtra("category") != null) {
            category = intent.getStringExtra("category");
        }
        SetSpinner();
        SetRecyclerView(null, category);
        super.onResume();
    }

    // 뒤로 가기 버튼을 눌렀을 때
    @Override
    public void onBackPressed() {
        if (is_category_open) {
            ChangeCategoryScreen();
        } else {
            finish();
        }
    }

    // 키보드 열린거 닫기
    public void CloseKeyBoard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive(et)) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    // 스피너의 아이템 설정하기
    public void SetSpinner() {
        // 만약 창으로 돌아왔을 때, 이전의 카테고리를 가진 표가 없을 경우, 종료함
        if (table_adapter.items.size() != 0) {
            String[][] array = db_helper.InquiryNameAndCategory(null, category);
            if (array.length != 0) {
                return;
            }
        }
        // 카테고리 조회하기
        categories = db_helper.InquiryCategory();
        // 분류 없음 추가해서 배열 만들기
        String[] arr = new String[categories.length+1];
        for (int i=0; i<arr.length; i++) {
            // 첫번째 인덱스는 (분류 없음)으로
            if (i != 0) {
                arr[i] = categories[i-1];
            } else {
                arr[i] = "(분류 없음)";
            }
        }
        categories = arr;
        // 스피너 설정하기
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_L.setAdapter(category_adapter);
    }

    // 리싸이클러뷰의 아이템 설정하기
    public void SetRecyclerView(String name ,String category) {
        // 어뎁터가 비어있지 않다면
        if (table_adapter.items.size() != 0) {
            // 어뎁터의 데이터 모두 삭제
            table_adapter.RemoveAllItem();
        }
        // (분류 없음)으로 설정했다면,
        if (category == "(분류 없음)") {
            // 카테고리 비우기
            this.category = null;
        }
        // DB의 데이터를 어뎁터에 추가하기
        classified_tables = db_helper.InquiryNameAndCategory(name, this.category);
        for (int i=0; i<classified_tables.length; i++) {
            int color = db_helper.InquiryColor(classified_tables[i][0], classified_tables[i][1]);
            table_adapter.AddItem(
                    new ReviewRecord(classified_tables[i][0], classified_tables[i][1], color));
        }
        // 리싸이클러뷰에 어뎁터 설정하기
        table_list.setAdapter(table_adapter);
    }

    // 카테고리가 이미 존재하는가
    public boolean IsInArray(String name) {
        categories = db_helper.InquiryCategory();
        for (int i=0; i<classified_tables.length; i++) {
            if (classified_tables[i][0].contains(name)) {
                return true;
            }
        }
        return false;
    }

    // 투명화 애니메이션 리스너
    private class becoming_transparent_anime_listener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationEnd(Animation animation) {
            if (is_category_open) {
                create_category_screen.setVisibility(View.GONE);
                is_category_open = false;
            } else {
                is_category_open = true;
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    // 카테고리 화면의 애니메이션 설정
    public void ChangeCategoryScreen() {
        if (is_category_open) {
            create_category_screen.startAnimation(transparent_off);
        } else {
            create_category_screen.setVisibility(View.VISIBLE);
            create_category_screen.startAnimation(transparent_on);
        }
    }

    // 색상 선택하기
    public void ChoiceColor(int n, ImageView color) {
        if (color != selected) {
            color.setImageResource(images[n][1]);
            if (selected != null) {
                selected.setImageResource(images[color_num][0]);
            }
            selected = color;
            color_num = n;
        }
    }
}