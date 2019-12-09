package com.example.fooding;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fooding.Target.TargetList;

public class DBHelper {
    private static final String TAG = "DBHelper";

    private static SQLiteDatabase database;

    private static String createTableLikeSql = "create table IF NOT EXISTS outline" +
            "(" +
            "    _id integer PRIMARY KEY autoincrement, " +
            "    name text, " +
            "    address text, " +
            "    description text, " +
            "    image text" +
            ")";

    // TODO : Target 로칼 저
    private static String createTableTargetSql = "create table IF NOT EXISTS details" +
            "(" +
            "    _id integer PRIMARY KEY autoincrement " +
            ")"; //소괄호안에 칼럼정보

    //SQL 문을 어떻게 생성을 하는지 그리고 테이블들 만들고 그다음에 데이터를 추가하거나 조회하기  //create table 만들 때 _id 라고 하는 게 SQLite에서는 내부적으로 내부 id로 관리가 됨
    public static void openDatabase(Context context, String databaseName) {
        println("openDatabse 호출됨.");

        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (database != null) {
                println("데이터베이스 : " + databaseName + " 오픈됨.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName) {
        println("createTable 호출됨 : " + tableName);

        if (database != null) {     //SQL 문을 여기다 넣으면 좀 복잡해지니까 위에 문자열을 미리 만들어놓기
            if (tableName.equals("like")) {  //영화 목록화면 용 테이블
                database.execSQL(createTableLikeSql);
                println("outline 테이블 생성 요청됨.");
            } else if (tableName.equals("target")) {  //영화 상세화면 용 테이블
                database.execSQL(createTableTargetSql);
                println("details 테이블 생성 요청됨.");
            }

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void insertLikeData(String name, String address, String description, String image) {
        println("insertData() 호출됨.");

        if (database != null) {
            String sql = "insert or replace into outline(name, address, description, image) values(?, ?, ?, ?, ?)";
            Object[] params = {name, address, description, image};

            database.execSQL(sql, params);

            println("데이터 추가함.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    public TargetList selectLikeData(String tableName) {
        println("selectData() 호출됨.");

        if (database != null) {
            String sql = "select name, address, description, image from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);
            println("조회된 데이터 개수 : " + cursor.getCount());

            for (int i = 0; i< cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                String address = cursor.getString(1);
                String description = cursor.getString(2);
                String image = cursor.getString(4);

                println("#" + i + " -> " + name + ", " + address + ", " + description + ", " + image);
            }

            cursor.close();
        }
        return null;
    }

    public static void println(String data) {
        Log.d(TAG, data);
    }
}

