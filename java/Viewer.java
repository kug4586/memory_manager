package com.example.android_app;

public class Viewer {
    // 변수
    String date;
    Boolean is_reviewed;

    // 생성자
    public Viewer(String date, Boolean is_reviewed) {
        this.date = date;
        this.is_reviewed = is_reviewed;
    }

    // get~ : 실행하는 위치로 내용물 보냄
    // set~ : 실행하는 위치에서 내용물 가져옴

    public String GetDate() {
        return date;
    }
    public void SetDate(String date) {
        this.date = date;
    }

    public Boolean GetChecked() {
        return is_reviewed;
    }
    public void SetChecked(boolean is_reviewed) {
        this.is_reviewed = is_reviewed;
    }
}
