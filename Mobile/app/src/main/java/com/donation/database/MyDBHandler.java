package com.donation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "donation.db";
    public static final String TABLE_TOKEN = "token";
    public static final String COLOUM_ID = "id";
    public static final String COLOUM_TOKEN = "token";

    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = " CREATE TABLE " + TABLE_TOKEN + " ( " + COLOUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLOUM_TOKEN + " TEXT " + ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_TOKEN);
        onCreate(sqLiteDatabase);
    }

    public void addToken(String token) {
        ContentValues value = new ContentValues();
        value.put(COLOUM_TOKEN, token);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_TOKEN, null, value);
        sqLiteDatabase.close();
    }

    public void deleteToken(String token) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TOKEN, COLOUM_TOKEN + "=?", new String[]{token});
        sqLiteDatabase.close();
    }

    public String getUserToken() {
        /*SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(" SELECT token FROM token; ", null);
        String token = "";
        if(c.getCount() > 0){
            c.moveToFirst();
            token = c.getString(0);
            sqLiteDatabase.close();
        }
        return token;*/
        return "b83b5620-a943-46b7-bf53-ab9880bbc1c6";
    }
}
