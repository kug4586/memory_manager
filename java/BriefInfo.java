package com.example.android_app;

public class BriefInfo {
    // 변수
    String tag;
    int color;

    // 생성자
    public BriefInfo(String tag, int color) {
        this.tag = tag;
        this.color = color;
    }

    // get~ : 실행하는 위치로 내용물 보냄
    // set~ : 실행하는 위치에서 내용물 가져옴

    public String GetTag() {
        return tag;
    }
    public void SetTag(String tag) {
        this.tag = tag;
    }

    public int GetColor() {
        return color;
    }
    public void SetColor(int color) {
        this.color = color;
    }
}
