package com.example.android_app;

public class Viewer {
    // 변수
    String date;

    // 생성자
    public Viewer(String date) {
        this.date = date;
    }

    // get~ : 실행하는 위치로 내용물 보냄
    // set~ : 실행하는 위치에서 내용물 가져옴

    public String GetDate() {
        return date;
    }

    public void SetDate(String date) {
        this.date = date;
    }
}
