package com.todo.group1.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin Banks on 7/23/16.
 * Manages a local database for tasks
 */
public class ToDoDbHelper extends SQLiteOpenHelper {

    // must be incremented upon schema change
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "todo.db";

    public ToDoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
