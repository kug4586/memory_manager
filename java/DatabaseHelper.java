package com.example.android_app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;

    // 데이터베이스의 변수
    public static String DB_NAME = "memory_manager";
    public static String REVIEW_LIST_NAME = "review_list";
    public static String CATEGORY_LIST_NAME = "category_list";
    public static int VERSION = 1;
    public int table_count;
    public String sql;

    String a = "1st";

    // 생성자
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // 데이터베이스가 생성될 때
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 암기표 목록이 없다면, 생성
        db.execSQL("create table if not exists " + REVIEW_LIST_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, category text, "
                + " _1st text, _2nd text, _3rd text, _4th text, _5th text, "
                + " _6th text, _7th text, _8th text, _9th text, _10th text"
                + ")");
        // 카테고리 목록이 없다면, 생성
        db.execSQL("create table if not exists " + CATEGORY_LIST_NAME + "(category text PRIMARY KEY)");
    }

    // 데이터베이스 열었을 때
    @Override
    public void onOpen(SQLiteDatabase db) {
        database = db;
        super.onOpen(db);
    }

    // 데이터베이스 업그레이드할 때
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_ver, int new_ver) {
        if (new_ver > 1) {
            db.execSQL("drop table if exists " + REVIEW_LIST_NAME);
        }
    }

    // table_count 가져오기
    public void GetTableCount(int n) {
        this.table_count = n;
    }

    // table_count 전송하기
    public int SetTableCount() {
        return table_count;
    }

    // 카테고리 추가
    public void CreateCategory(String name) {
        database.execSQL("insert into " + CATEGORY_LIST_NAME + " values ( ' " + name + " ' )");
    }

    // 카테고리 수정
    public void ModifyCategory(String current_name, String alter) {
        database.execSQL("update " + CATEGORY_LIST_NAME + " set category = ' " + alter + " ' where category = ' " + current_name + " '");
    }

    // 카테고리 삭제
    public void DeleteCategory(String name) {
        database.execSQL("delete from " + CATEGORY_LIST_NAME + " where category = ' " + name + " '");
    }

    // 하루에 외울 것을 뽑아내기
    public void FilterDailyReview() {

    }

    // 암기표 추가
    public void CreateReviewTable(String table_name, String category, String[] dates, int review_count) {
        // 암기표 목록에 추가
        database.execSQL("insert into " + REVIEW_LIST_NAME
                + " values ("
                + " ' " + table_name + " ', ' " + category +" ',"
                + " ' " + dates[0] + " ', ' " + dates[1] + " ',"
                + " ' " + dates[2] + " ', ' " + dates[3] + " ',"
                + " ' " + dates[4] + " ', ' " + dates[5] + " ',"
                + " ' " + dates[6] + " ', ' " + dates[7] + " ',"
                + " ' " + dates[8] + " ', ' " + dates[9] + " ')"
        );
        // 암기표 만들기 (테이블명은 숫자로)
        sql = "create table if not exists " + table_count + "("
                + " name text, "
                + " category text, "
                + " " + dates[0] + " boolean, "
                + " " + dates[1] + " boolean, "
                + " " + dates[2] + " boolean, "
                + " " + dates[3] + " boolean, "
                + " " + dates[4] + " boolean, ";
        if (review_count > 5) {
            sql = sql
                    + " " + dates[5] + " boolean, "
                    + " " + dates[6] + " boolean, ";
            if (review_count > 7) {
                sql = sql
                        + " " + dates[7] + " boolean, "
                        + " " + dates[8] + " boolean, "
                        + " " + dates[9] + " boolean, ";
            }
        }
        sql = sql + " PRIMARY KEY(name, category))";
        database.execSQL(sql);
        // 암기표 초기 설정
        sql = "insert into " + table_count + " values "
                + "( ' " + table_name + " ', ' "+ category + " ', "
                + " false, false, false, false, false, ";
        if (review_count > 5) {
            sql = sql + " false, false, ";
            if (review_count > 7) {
                sql = sql + " false, false, false";
            }
        }
        sql = sql + ")";
        database.execSQL(sql);
        // 다음에 만들 암기표를 위해 count 증가
        table_count++;
    }

    // 해당 날짜에 암기했음을 표시
    public void DailyRemembered(int num, String date) {
        database.execSQL("update " + num + " set " + date + " = true");
    }

    // 암기표 삭제
    public void DeleteReviewTable(int num) {
        database.execSQL("drop table if exists " + num);
    }
}
