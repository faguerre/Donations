package com.donation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class TokenHandler extends DBHandler {

    public TokenHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void addToken(String token) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_TOKEN, token);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_TOKEN, null, value);
        sqLiteDatabase.close();
    }

    public void deleteToken(String token) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TOKEN, COLUMN_TOKEN + "=?", new String[]{token});
        sqLiteDatabase.close();
    }

    public String getUserToken() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = " SELECT " + COLUMN_TOKEN + " FROM " + TABLE_TOKEN + "; ";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        String token = "";
        if (c.getCount() > 0) {
            c.moveToFirst();
            token = c.getString(0);
            sqLiteDatabase.close();
        }
        return token;
    }
}
