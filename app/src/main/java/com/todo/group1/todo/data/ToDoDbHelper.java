package com.todo.group1.todo.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todo.group1.todo.R;
import com.todo.group1.todo.data.ToDoContract.TaskEntry;
import com.todo.group1.todo.data.ToDoContract.TaskLabel;
import com.todo.group1.todo.data.ToDoContract.TaskPriority;




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

        // Create a table to hold our labels
        final String SQL_CREATE_LABEL_TABLE = "CREATE TABLE" + TaskLabel.TABLE_NAME + " (" +
                TaskLabel._ID + " INTEGER PRIMARY KEY," +
                TaskLabel.COLUMN_LABEL + " TEXT NOT NULL " +
                " );";

        // Create a table to hold our priorities
        final String SQL_CREATE_PRIORITY_TABLE = "CREATE TABLE" + TaskPriority.TABLE_NAME + " (" +
                TaskPriority._ID + " INTEGER PRIMARY KEY," +
                TaskPriority.COLUMN_PRIORITY + " TEXT NOT NULL " +
                " );";

        final String SQL_INSERT_PRIORITY_TABLE = "INSERT INTO " + TaskLabel.TABLE_NAME +
                " VALUES (" + Resources.getSystem().getString(R.string.priority_none) + ")," +
                " (" + Resources.getSystem().getString(R.string.priority_low) + ")," +
                " (" + Resources.getSystem().getString(R.string.priority_med) + ")," +
                " (" + Resources.getSystem().getString(R.string.priority_high) +
                ");";

        // Create a table to hold our tasks
        final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TaskEntry.COLUMN_CREATE_DATE + " TEXT NOT NULL," +
                TaskEntry.COLUMN_DUE_DATE + " TEXT," +
                TaskEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                TaskEntry.COLUMN_DETAIL + " TEXT," +
                TaskEntry.COLUMN_IS_COMPLETED + " INTEGER NOT NULL," +
                TaskEntry.COLUMN_IS_DELETED + " INTEGER NOT NULL," +
                TaskEntry.COLUMN_REMINDER_ADDED + " INTEGER NOT NULL," +
                TaskEntry.COLUMN_LABEL_ID + " INTEGER NOT NULL," +
                TaskEntry.COLUMN_PRIORITY_ID + " INTEGER NOT NULL," +
                TaskEntry.COLUMN_PARENT_TASK_ID + " INTEGER," +

                // Set up the priority column as a foreign key to the priority table
                " FOREIGN KEY (" + TaskEntry.COLUMN_PRIORITY_ID + ") REFERENCES " +
                TaskPriority.TABLE_NAME + "(" + TaskPriority._ID + "), " +

                // Set up label column as a foreign key to the label table
                " FOREIGN KEY (" + TaskEntry.COLUMN_LABEL_ID + ") REFERENCES " +
                TaskLabel.TABLE_NAME + "(" + TaskLabel._ID + "));";

        sqLiteDatabase.execSQL(SQL_CREATE_LABEL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRIORITY_TABLE);
        sqLiteDatabase.execSQL(SQL_INSERT_PRIORITY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TASK_TABLE);
    }

    // this function to be implemented if DATABASE_VERSION is incremented (i.e. if schema changes)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
