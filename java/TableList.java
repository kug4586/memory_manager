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

        // id??? ??? ?????? ????????????
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


        // ???????????????
        transparent_on = AnimationUtils.loadAnimation(this, R.anim.transparent_on);
        transparent_off = AnimationUtils.loadAnimation(this, R.anim.transparent_off);
        // ??????????????? ?????????
        becoming_transparent_anime_listener appearing_anim_listener = new becoming_transparent_anime_listener();
        transparent_on.setAnimationListener(appearing_anim_listener);
        transparent_off.setAnimationListener(appearing_anim_listener);


        // ???????????? ??????
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ?????? ?????????
                if (selected != null) {
                    selected.setImageResource(images[color_num-1][0]);
                }
                color_num = -1;
                selected = null;
                category_name.setText("");
                ChangeCategoryScreen();
            }
        });
        // ???????????? ?????? ????????? ???
        create_category_screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CloseKeyBoard(category_name);
                return true;
            }
        });
        // ?????? ????????????
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
        // ???????????? ????????? ??????
        create_category_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCategoryScreen();
                CloseKeyBoard(category_name);
            }
        });
        // ???????????? ????????? ??????
        create_category_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = category_name.getText().toString();
                // ????????? ????????? ????????? ????????? ???????????????
                if (category_name.length() != 0 && color_num > -1) {
                    // ?????? ???????????? ??????????????????
                    if (IsInArray(name)) {
                        Toast.makeText(getApplicationContext(), "?????? ???????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        // DB??? ????????????
                        db_helper.CreateCategory(name, color_num);
                        // ??? ??????
                        SetSpinner(true);
                        ChangeCategoryScreen();
                        CloseKeyBoard(category_name);
                    }
                } else {
                    // ????????? ???????????? ????????? ???????????? ????????????
                    if (category_name.length() == 0 && color_num < 0) {
                        Toast.makeText(getApplicationContext(), "????????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
                    // ????????? ???????????? ????????????
                    } else if (color_num < 0) {
                        Toast.makeText(getApplicationContext(), "????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                    // ????????? ???????????? ????????????
                    } else {
                        Toast.makeText(getApplicationContext(), "????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // ???????????? ????????????
        delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ?????? ????????? ??????
                String[] arr = new String[categories.length-1];
                boolean[] check = new boolean[categories.length-1];
                for (int i=0; i<arr.length; i++) {
                    arr[i] = categories[i+1];
                }
                for (int i=0; i<check.length; i++) {
                    check[i] = false;
                }
                // ?????? ??????????????? ?????????
                if (arr.length != 0) {
                    // ?????? ?????? ?????????
                    AlertDialog.Builder dlg = new AlertDialog.Builder(TableList.this);
                    dlg.setTitle("???????????? ??????")
                            .setMultiChoiceItems(arr, check, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    check[i] = b;
                                }
                            })
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                                                .setTitle("??????!")
                                                .setMessage("????????? ??????????????????????")
                                                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        RemoveCategory(check, arr);
                                                        SetSpinner(false);
                                                    }
                                                })
                                                .setNegativeButton("?????????", null);
                                        last.show();
                                    } else {
                                        AlertDialog.Builder error = new AlertDialog.Builder(TableList.this)
                                                .setMessage("????????? ??????????????? ????????????")
                                                .setPositiveButton("??????", null);
                                        error.show();
                                    }
                                }
                            })
                            .setNegativeButton("??????", null)
                            .show();
                } else {
                    AlertDialog.Builder error = new AlertDialog.Builder(TableList.this)
                            .setTitle("??????!")
                            .setMessage("??????????????? ????????????")
                            .setPositiveButton("??????", null);
                    error.show();
                }
            }
        });


        // ???????????? ????????? ?????? ???????????? ???
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
                    // ?????????????????? ????????????
                    SetRecyclerView(null, category);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        // ????????? ??????
        LinearLayoutManager manager = new LinearLayoutManager(
                getApplicationContext(), RecyclerView.VERTICAL, false);
        table_list.setLayoutManager(manager);
        table_adapter = new ReviewRecordAdapter();
        // ?????? ?????????
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
        SetSpinner(false);
        SetRecyclerView(null, category);
        super.onResume();
    }

    // ?????? ?????? ????????? ????????? ???
    @Override
    public void onBackPressed() {
        if (is_category_open) {
            ChangeCategoryScreen();
        } else {
            finish();
        }
    }

    // ????????? ????????? ??????
    public void CloseKeyBoard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive(et)) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    // ???????????? ????????? ????????????
    public void SetSpinner(Boolean making) {
        // ?????? ????????? ???????????? ???, ????????? ??????????????? ?????? ?????? ?????? ??????, ?????????
        if (table_adapter.items.size() != 0 && !making) {
            String[][] array = db_helper.InquiryNameAndCategory(null, category);
            if (array.length != 0) {
                return;
            }
        }
        // ???????????? ????????????
        categories = db_helper.InquiryCategory();
        // ?????? ?????? ???????????? ?????? ?????????
        String[] arr = new String[categories.length+1];
        for (int i=0; i<arr.length; i++) {
            // ????????? ???????????? (?????? ??????)??????
            if (i != 0) {
                arr[i] = categories[i-1];
            } else {
                arr[i] = "(?????? ??????)";
            }
        }
        categories = arr;
        // ????????? ????????????
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_L.setAdapter(category_adapter);
    }

    // ????????????????????? ????????? ????????????
    public void SetRecyclerView(String name ,String category) {
        // ???????????? ???????????? ?????????
        if (table_adapter.items.size() != 0) {
            // ???????????? ????????? ?????? ??????
            table_adapter.RemoveAllItem();
        }
        // (?????? ??????)?????? ???????????????,
        if (category == "(?????? ??????)") {
            // ???????????? ?????????
            this.category = null;
        }
        // DB??? ???????????? ???????????? ????????????
        classified_tables = db_helper.InquiryNameAndCategory(name, this.category);
        for (int i=0; i<classified_tables.length; i++) {
            int color = db_helper.InquiryColor(classified_tables[i][0], classified_tables[i][1]);
            table_adapter.AddItem(
                    new ReviewRecord(classified_tables[i][0], classified_tables[i][1], color));
        }
        // ????????????????????? ????????? ????????????
        table_list.setAdapter(table_adapter);
    }

    // ??????????????? ?????? ???????????????
    public boolean IsInArray(String name) {
        categories = db_helper.InquiryCategory();
        for (int i=0; i<classified_tables.length; i++) {
            if (classified_tables[i][0].contains(name)) {
                return true;
            }
        }
        return false;
    }

    // ????????? ??????????????? ?????????
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

    // ???????????? ????????? ??????????????? ??????
    public void ChangeCategoryScreen() {
        if (is_category_open) {
            create_category_screen.startAnimation(transparent_off);
        } else {
            create_category_screen.setVisibility(View.VISIBLE);
            create_category_screen.startAnimation(transparent_on);
        }
    }

    // ?????? ????????????
    public void ChoiceColor(int n, ImageView color) {
        if (color != selected) {
            color.setImageResource(images[n][1]);
            if (selected != null) {
                selected.setImageResource(images[color_num-1][0]);
            }
            selected = color;
            color_num = n+1;
        }
    }

    // ???????????? ????????????
    public void RemoveCategory(boolean[] check, String[] arr) {
        for (int k=0; k<arr.length; k++) {
            if (check[k]) {
                // DB?????? ??????
                db_helper.DeleteCategory(arr[k]);
                // ReviewRecord??? ?????? ?????????
                if (table_adapter.items.size() != 0) {
                    ReviewRecord item = table_adapter.GetItem(k);
                    item.SetColor(0);
                    table_adapter.notifyItemChanged(k);
                }
            }
        }
    }
}