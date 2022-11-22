package com.i192048.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHandler extends SQLiteOpenHelper {

    String CREATE_USER_TABLE = "CREATE TABLE " +
            UserContract.Project.TABLE_NAME + "("+
            UserContract.Project._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            UserContract.Project._NAME + " TEXT NOT NULL , "+
            UserContract.Project._USERNAME + " TEXT NOT NULL , "+
            UserContract.Project._PHONE + " TEXT NOT NULL , "+
            UserContract.Project._ADDRESS + " TEXT NOT NULL , "+
            UserContract.Project._EMAIL + " TEXT NOT NULL , "+
            UserContract.Project._PASSWORD + " TEXT NOT NULL ); ";

    String DELETE_USER_TABLE = " DROP TABLE IF EXISTS " + UserContract.Project.TABLE_NAME;

    public UserDBHandler(@Nullable Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_USER_TABLE);
        onCreate(sqLiteDatabase);
    }
}
