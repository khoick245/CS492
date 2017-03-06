package com.nkdroid.tinderswipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

/**
 * Created by omid on 3/2/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DataBase_Name = "restuarant.db";
    public static final String Table_Name = "Liked";
    public static final String ID = "YelpRest";
    public DBHelper(Context context) {
        super(context, DataBase_Name, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" Create Table " + Table_Name + " (ID STRING PRIMARY KEY )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        onCreate(db);
    }

    public boolean insertData(String rest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, rest);
        long result = db.insert(Table_Name, null, contentValues);
        if (result==-1) {
            return false;
        }
        else return true;
    }
}