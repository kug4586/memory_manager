package com.example.android_app;

public class DailyTable {
    // 사용할 변수
    String table_name;
    String count_of_repeat;
    String category;

    // 객체 생성
    public DailyTable(String table_name, String category, String count_of_repeat) {
        this.table_name = table_name;
        this.count_of_repeat = count_of_repeat;
        this.category = category;
    }

    // get~ : 실행하는 위치로 내용물 보냄
    // set~ : 실행하는 위치에서 내용물 가져옴

    public String get_table_name() {
        return table_name;
    }
    public void set_table_name(String table_name) {
        this.table_name = table_name;
    }

    public String get_count_of_repeat() {
        return count_of_repeat;
    }
    public void set_count_of_repeat(String count_of_repeat) {
        this.count_of_repeat = count_of_repeat;
    }

    public String GetCategory() {
        return category;
    }
    public void SetCategory(String category) {
        this.category = category;
    }
}
