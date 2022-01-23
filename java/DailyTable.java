package com.example.android_app;

public class DailyTable {
    // 사용할 변수
    String table_name;
    String count_of_repeat;

    // 객체 생성
    public DailyTable(String table_name, String count_of_repeat) {
        this.table_name = table_name;
        this.count_of_repeat = count_of_repeat;
    }

    // get~ : 내용물 가져오기
    // set~ : 내용물 집어넣기

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
}
