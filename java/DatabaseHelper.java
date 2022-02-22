package com.example.android_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

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
                + " name text, category text, reviewed integer, today boolean, "
                + " _1st text, _2nd text, _3rd text, _4th text, _5th text, "
                + " _6th text, _7th text, _8th text, _9th text, _10th text "
                + ")");
        // <카테고리 목록>이 없으면 만들기
        db.execSQL("create table if not exists " + CATEGORY_LIST_NAME
                + "(_id integer PRIMARY KEY autoincrement, category text, color integer)");
    }

    // 데이터베이스 열 때
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
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
                + "(name, category, reviewed, today, _1st, _2nd, _3rd, _4th, _5th, _6th, _7th, _8th, _9th, _10th)"
                + " values ("
                + "'" + name + "', '" + category + "', 1, false, "
                + "'"+ dates[0] + "', '" + dates[1] + "', '" + dates[2] + "', '" + dates[3] + "', '" + dates[4] + "', "
                + "'"+ dates[5] + "', '" + dates[6] + "', '" + dates[7] + "', '" + dates[8] + "', '" + dates[9] + "'"
                + ")");
    }

    // 복습표 삭제하기
    public void DeleteReviewTable(String name, String category) {
        database.execSQL("delete from " + REVIEW_LIST_NAME
                + " where name='" + name + "' and category='" + category + "'");
    }

    // 복습했음을 기록하기
    public void RecordReview(String name, String category) {
        database.execSQL("update " + REVIEW_LIST_NAME
                + " set reviewed = reviewed + 1, today = true"
                + " where name='" + name + "' and category='" + category + "'");
    }

    // 복습 초기화
    public void ResetReview() {
        database.execSQL("update " + REVIEW_LIST_NAME + " set today=false");
    }

    // 하루 할당량을 채웠는지
    public boolean IsNotFinished() {
        // 오늘 날짜
        String date = new SimpleDateFormat("MM/dd").format(new Date());
        // 실행할 sql 생성하기
        sql = "select today from " + REVIEW_LIST_NAME
                + " where _1st='" + date + "' or _2nd='" + date + "' or _3rd='" + date + "'"
                + " or _4th='" + date + "' or _5th='" + date + "' or _6th='" + date + "'"
                + " or _7th='" + date + "' or _8th='" + date + "' or _9th='" + date + "' or _10th='" + date + "'";
        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);
        int record_count = cursor.getCount();

        // 데이터 조사하기
        for (int i=0; i<record_count; i++) {
            cursor.moveToNext();
            if (cursor.getInt(0) == 0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
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
                cursor.getString(10)
        };
        cursor.close();
        return result;
    }

    // 카테고리와 이름으로 복습표 이름과 카테고리 조회하기
    public String[][] InquiryNameAndCategory(String name, String category) {
        // 실행할 sql 생성하기
        sql = "select name, category from " + REVIEW_LIST_NAME;
        if (name != null && category != null) {
            sql = sql + " where name='" + name + "' and category='" + category + "'";
        } else if (name != null && category == null) {
            sql = sql + " where name='" + name + "'";
        } else if (name == null && category != null) {
            sql = sql + " where category='" + category + "'";
        }

        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);
        int record_count = cursor.getCount();

        // 배열에 저장하기
        String[][] result = new String[record_count][2];
        for (int i=0; i<record_count; i++) {
            cursor.moveToNext();
            String table_name = cursor.getString(0);
            String tag = cursor.getString(1);
            result[i][0] = table_name;
            result[i][1] = tag;
        }
        cursor.close();
        return result;
    }

    // 이름과 카테고리로 색상과 카테고리 조회하기
    public int InquiryColor(String name, String category) {
        // 실행할 sql 생성하기
        sql = "select color from " + CATEGORY_LIST_NAME
                + " where category=("
                + "select category from " + REVIEW_LIST_NAME
                + " where name='" + name + "' and category='" + category + "'"
                + ")";
        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToNext();
        int color = cursor.getInt(0);
        cursor.close();
        return color;
    }

    // 오늘자 외울 것들 가져오기
    public String[][] FilterDailyTable(String today) {
        // 실행할 sql 생성하기
        sql = "select name, category, reviewed from " + REVIEW_LIST_NAME
                + " where today=false and (_1st='" + today + "' or _2nd='" + today + "' or _3rd='" + today + "' "
                + "or _4th='" + today + "' or _5th='" + today + "' or _6th='" + today + "' "
                + "or _7th='" + today + "' or _8th='" + today + "' or _9th='" + today + "' or _10th='" + today + "')";

        // 커서로 데이터 불러오기
        Cursor cursor = database.rawQuery(sql, null);
        int record_count = cursor.getCount();

        // 배열에 저장하기
        String[][] result = new String[record_count][3];
        for (int i=0; i<record_count; i++) {
            cursor.moveToNext();
            String name = cursor.getString(0);      // 이름은 문자열
            String category = cursor.getString(1);  // 카테고리는 문자열
            int count = cursor.getInt(2);           // 반복 횟수는 정수
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
            result[i][1] = category;
            result[i][2] = _count;
        }
        cursor.close();
        return result;
    }

    // 카테고리별 색깔과 테이블의 개수를 조회하기
    public int[][] InquiryColorAndCount() {
        // 카테고리 목록에서 카테고리 이름과 색상 정보 가져오기
        Cursor c1 = database.rawQuery("select category,color from " + CATEGORY_LIST_NAME, null);
        int count_of_c1 = c1.getCount();

        // 배열에 데이터 저장하기
        int[][] CnC = new int[2][count_of_c1];
        for (int i=0; i<count_of_c1; i++) {
            c1.moveToNext();
            // 테이블 개수
            sql = "select count(category) from " + REVIEW_LIST_NAME
                    + " where category='" + c1.getString(0) + "'";
            Cursor c2 = database.rawQuery(sql, null);
            c2.moveToNext();
            CnC[0][i] = c2.getInt(0);
            // 색상
            CnC[1][i] = c1.getInt(1);
            c2.close();
        }
        c1.close();
        return CnC;
    }

    // 카테고리 만들기
    @NonNull
    public void CreateCategory(String name, int color) {
        database.execSQL("insert into " + CATEGORY_LIST_NAME
                + "(category, color)"
                + " values "
                + "('" + name + "', " + color + ")");
    }

    // 카테고리 삭제하기
    public void DeleteCategory(String category) {
        // 카테고리 목록에서 삭제
        database.execSQL("delete from " + CATEGORY_LIST_NAME
                + " where category='" + category + "'");
        // 복슾표 목록에서 삭제
        database.execSQL("update " + REVIEW_LIST_NAME
                + " set category='none'"
                + " where category='" + category + "'");
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
        cursor.close();
        return result;
    }
}
