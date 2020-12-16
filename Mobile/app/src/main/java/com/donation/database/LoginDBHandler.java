package com.donation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.donation.model.in.UserModelIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class LoginDBHandler extends DBHandler {


    public LoginDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void addUserGoogle(GoogleSignInAccount account) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_TOKEN, account.getId());
        value.put(COLUMN_EMAIL, account.getEmail());
        value.put(COLUMN_NAME, account.getDisplayName());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_USER, null, value);
        sqLiteDatabase.close();
    }

    public void addUser(String token, UserModelIn user,String external) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_TOKEN, token);
        value.put(COLUMN_EMAIL, user.getEmail());
        value.put(COLUMN_NAME, user.getName());
        value.put(COLUMN_EXTERNAL,external);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_USER, null, value);
        sqLiteDatabase.close();
    }

    public void deleteUser(String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_USER, COLUMN_EMAIL + "=?", new String[]{email});
        sqLiteDatabase.close();
    }

    public boolean existsUser() {
        boolean exists = false;

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(" SELECT " + COLUMN_EMAIL + "," + COLUMN_NAME + " FROM " + TABLE_USER + "; ", null);
        if (c.getCount() > 0) {
            exists = true;
        }
        sqLiteDatabase.close();
        return exists;
    }
    public boolean isExternal() {
        boolean exists = false;

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(" SELECT " + COLUMN_EXTERNAL + " FROM " + TABLE_USER + "; ", null);
        if (c.getCount() > 0) {
            c.moveToFirst();

            exists = (c.getString(0)==null? true:false);
        }
        sqLiteDatabase.close();
        return exists;
    }

    public UserModelIn getUser() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(" SELECT " + COLUMN_EMAIL + "," + COLUMN_NAME + " FROM " + TABLE_USER + "; ", null);
        UserModelIn user = null;
        if (c.getCount() > 0) {
            user = new UserModelIn();
            c.moveToFirst();
            user.setEmail(c.getString(0));
            user.setName(c.getString(1));
            sqLiteDatabase.close();
        }
        return user;
    }

    public List<UserModelIn> getAllUser() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(" SELECT " + COLUMN_EMAIL + "," + COLUMN_NAME + " FROM " + TABLE_USER + "; ", null);

        UserModelIn user;
        List<UserModelIn> listOfUsers = new ArrayList<UserModelIn>();

        try {
            c.moveToFirst();
            while (c.moveToNext()) {
                user = new UserModelIn();
                user.setEmail(c.getString(0));
                user.setName(c.getString(1));
                listOfUsers.add(user);
            }
        } catch (Exception e) {
            Log.println(Log.ERROR, "Login", e.getMessage());
        } finally {
            c.close();
            sqLiteDatabase.close();
        }

        return listOfUsers;
    }
}
