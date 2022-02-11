package com.example.android_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스 변수
    public SQLiteDatabase database;
    public static String DB_NAME = "MemoryManager";
    public static String REVIEW_LIST_NAME = "review_list";
    public static String CATEGORY_LIST_NAME = "category_list";
    public static int VERSION = 1;
    public String sql;

    // 생성자
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        database = getWritableDatabase();
    }

    // 데이터베이스 생성될 때
    @Override
    public void onCreate(SQLiteDatabase db) {
        // <복습표 목록>이 없으면 만들기
        db.execSQL("create table if not exists " + REVIEW_LIST_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, category text, reviewed integer, "
                + " _1st text, _2nd text, _3rd text, _4th text, _5th text, "
                + " _6th text, _7th text, _8th text, _9th text, _10th text "
                + ")");
        // <카테고리 목록>이 없으면 만들기
        db.execSQL("create table if not exists " + CATEGORY_LIST_NAME
                + "(category text PRIMARY KEY, color integer)");
        // <카테고리 목록>의 초기 설정
        db.execSQL("insert into " + CATEGORY_LIST_NAME
                + " values ('분류 없이', null)");
        Log.d("DB", "onCreate 호출됨");
    }

    // 데이터베이스 열 때
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("DB", "onOpen 호출됨");
    }

    // 데이터베이스 업그레이드 할 때
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_ver, int new_ver) {
        if (new_ver > 1) {
            db.execSQL("drop table if exists " + REVIEW_LIST_NAME);
            db.execSQL("drop table if exists " + CATEGORY_LIST_NAME);
        }
    }

    // 복습표 만들기
    @NonNull
    public void CreateReviewTable(String name, String category, String[] dates) {
        database.execSQL("insert into " + REVIEW_LIST_NAME
                + "(name, category, reviewed, _1st, _2nd, _3rd, _4th, _5th, _6th, _7th, _8th, _9th, _10th)"
                + " values ("
                + "'" + name + "', '" + category + "', 1, "
                + "'"+ dates[0] + "', '" + dates[1] + "', '" + dates[2] + "', '" + dates[3] + "', '" + dates[4] + "', "
                + "'"+ dates[5] + "', '" + dates[6] + "', '" + dates[7] + "', '" + dates[8] + "', '" + dates[9] + "'"
                + ")");
    }

    // 복습표 정보 불러오기
    public String[] InquiryTable(String name, String category) {
        // 실행할 sql 생성
        sql = "select reviewed, _1st, _2nd, _3rd, _4th, _5th, _6th, _7th, _8th, _9th, _10th from "
                + REVIEW_LIST_NAME + " where name='" + name + "' and category='" + category + "'";

        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);

        // 배열에 데이터 저장하기
        cursor.moveToNext();
        String[] result = {
                "" + cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
        };

        return result;
    }

    // 카테고리로 복습표 이름 조회하기
    public String[] InquiryTableName(String category) {
        // 실행할 sql 생성하기
        sql = "select name from " + REVIEW_LIST_NAME;
        if (category != null) {
            sql = sql + " where category='" + category + "'";
        }

        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);
        int record_count = cursor.getCount();

        // 배열에 저장하기
        String[] result = new String[record_count];
        for (int i=0; i<record_count; i++) {
            cursor.moveToNext();
            String name = cursor.getString(0);
            result[i] = name;
        }

        return result;
    }

    // 오늘자 외울 것들 가져오기
    public String[][] FilterDailyTable(String today) {
        // 실행할 sql 생성하기
        sql = "select name, reviewed from " + REVIEW_LIST_NAME
                + " where _1st='" + today + "' or _2nd='" + today + "' or _3rd='" + today + "' "
                + "or _4th='" + today + "' or _5th='" + today + "' or _6th='" + today + "' "
                + "or _7th='" + today + "' or _8th='" + today + "' or _9th='" + today + "' or _10th='" + today + "'";

        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);
        int record_count = cursor.getCount();

        // 배열에 저장하기
        String[][] result = new String[record_count][2];
        for (int i=0; i<record_count; i++) {
            cursor.moveToNext();
            String name = cursor.getString(0);  // 이름은 문자열
            int count = cursor.getInt(1);       // 반복 횟수는 정수
            String _count;

            // 반복 횟수를 문자열로 변환하는 과정
            if (count == 1) {
                _count = count + "st";
            } else if (count == 2) {
                _count = count + "nd";
            } else if (count == 3) {
                _count = count + "rd";
            } else {
                _count = count + "th";
            }

            // 배열에 넣기
            result[i][0] = name;
            result[i][1] = _count;
        }

        return result;
    }

    // 카테고리 만들기
    @NonNull
    public void CreateCategory(String name, int color) {
        database.execSQL("insert into " + CATEGORY_LIST_NAME
                + " values "
                + "('" + name + "', " + color + ")");
    }

    // 카테고리 조회하기
    public String[] InquiryCategory() {
        // 커서로 데이터 읽어들이기
        Cursor cursor = database.rawQuery("select category from " + CATEGORY_LIST_NAME, null);
        int record_count = cursor.getCount();

        // 배열에 저장하기
        String[] result = new String[record_count];
        for (int i=0; i<record_count; i++) {
            cursor.moveToNext();
            String category = cursor.getString(0);
            result[i] = category;
        }

        return result;
    }
}
