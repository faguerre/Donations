package com.donation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 8;
    protected static final String DATABASE_NAME = "donation.db";
    protected static final String TABLE_TOKEN = "token";
    protected static final String TABLE_USER = "user";
    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_TOKEN = "token";
    protected static final String COLUMN_NAME = "name";
    protected static final String COLUMN_EMAIL = "email";
    protected static final String COLUMN_EXTERNAL = "external";



    public DBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableToken());
        db.execSQL(createTableUser());
    }

    private String createTableUser() {

        final String COLUMN_ID = "id";
        final String COLUMN_TOKEN = "token";
        final String COLUMN_NAME = "name";
        final String COLUMN_EMAIL = "email";
        return " CREATE TABLE " + TABLE_USER + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TOKEN + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, "+ COLUMN_EXTERNAL + " TEXT "  + ");";

    }

    private String createTableToken() {
        return " CREATE TABLE " + TABLE_TOKEN + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TOKEN + " , " + COLUMN_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT " + ");";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_TOKEN);
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        onUpgrade(db, 1, 1);
    }

}
