package com.example.programmer.excelentnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Programmer on 25.07.2017.
 */

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        super(context, "WidgetTable", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentValues insert_data;
        db.execSQL("create table WidgetTable (id integer, value string);");

//        for(int i = 0; i < 6; ++i) {
//            insert_data = new ContentValues();
//            insert_data.put("value", 0);
//            db.insert("checkBoxTable", null, insert_data);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        int a = 0;
    }
}
