package com.example.appkabombi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context,"MemoryPicsDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table picsItem (ID INTEGER PRIMARY KEY AUTOINCREMENT,LABEL TEXT,DETAIL TEXT,DATE INTEGER,IMAGEPATH TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String label,String detail,int date, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LABEL",label);
        contentValues.put("DETAIL",detail);
        contentValues.put("DATE",date);
        contentValues.put("IMAGEPATH",imagePath);
        long result = db.insert("picsItem",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+"picsItem",null);
        return res;
    }

    public Cursor getRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from picsItem where id = " + id,null);
        return res;
    }




}
