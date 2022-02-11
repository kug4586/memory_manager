package com.example.android_app;

public class ReviewRecord {
    // 사용할 변수
    String table_name;

    // 생성자
    public ReviewRecord(String table_name) {
        this.table_name = table_name;
    }

    // get~ : 실행하는 위치로 내용물 보냄
    // set~ : 실행하는 위치에서 내용물 가져옴

    public String GetTableName() { return table_name; }

    public void SetTableName(String table_name) { this.table_name = table_name; }
}
