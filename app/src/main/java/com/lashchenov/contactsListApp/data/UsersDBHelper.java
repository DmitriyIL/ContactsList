package com.lashchenov.contactsListApp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static UsersDBHelper dbInstance;

    public static synchronized UsersDBHelper getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new UsersDBHelper(context);
        }
        return dbInstance;
    }


    private UsersDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.ID + " INTEGER NOT NULL, "
                + UserContract.UserEntry.COLUMN_ACTIVE + " NUMERIC NOT NULL, "
                + UserContract.UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_AGE + " INTEGER NOT NULL, "
                + UserContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_PHONE + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_COMPANY + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_EYE_COLOR + " INTEGER NOT NULL, "
                + UserContract.UserEntry.COLUMN_FRUIT + " INTEGER NOT NULL, "
                + UserContract.UserEntry.COLUMN_LATITUDE + " REAL NOT NULL, "
                + UserContract.UserEntry.COLUMN_LONGITUDE + " REAL NOT NULL, "
                + UserContract.UserEntry.COLUMN_REGISTERED + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_ABOUT + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_FRIENDS + " TEXT"
                + ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
