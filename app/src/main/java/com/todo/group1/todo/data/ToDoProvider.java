package com.todo.group1.todo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Justin Banks on 7/24/16.
 */

public class ToDoProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ToDoDbHelper mOpenHelper;

    static final int TASKS = 100;
    static final int TASKS_WITH_PRIORITY = 101;
    static final int TASKS_AFTER_DATE = 102;
    static final int TASKS_MARKED_COMPLETE = 103;
    static final int TASKS_WITH_LABEL = 104;

    static final int LABEL = 300;
    static final int LABELS_WITH_TASK = 301;

    static final int PRIORITY = 500;
    static final int PRIORITY_WITH_ID = 501;


    static UriMatcher buildUriMatcher() {
        //The code passed into the constructor represents the code to return for the root URI
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ToDoContract.CONTENT_AUTHORITY;

        // Use the addURI function to match each of the types
        matcher.addURI(authority, ToDoContract.PATH_TASK, TASKS);
        matcher.addURI(authority, ToDoContract.PATH_TASK, TASKS_WITH_PRIORITY);
        matcher.addURI(authority, ToDoContract.PATH_TASK, TASKS_AFTER_DATE);
        matcher.addURI(authority, ToDoContract.PATH_TASK, TASKS_MARKED_COMPLETE);
        matcher.addURI(authority, ToDoContract.PATH_TASK, TASKS_WITH_LABEL);

        matcher.addURI(authority, ToDoContract.PATH_LABEL, LABEL);
        matcher.addURI(authority, ToDoContract.PATH_LABEL, LABELS_WITH_TASK);

        matcher.addURI(authority, ToDoContract.PATH_PRIORITY, PRIORITY);
        matcher.addURI(authority, ToDoContract.PATH_PRIORITY, PRIORITY_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ToDoDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return ToDoContract.TaskEntry.CONTENT_TYPE;
            case TASKS_WITH_PRIORITY:
                return ToDoContract.TaskEntry.CONTENT_TYPE;
            case TASKS_AFTER_DATE:
                return ToDoContract.TaskEntry.CONTENT_TYPE;
            case TASKS_MARKED_COMPLETE:
                return ToDoContract.TaskEntry.CONTENT_TYPE;
            case TASKS_WITH_LABEL:
                return ToDoContract.TaskEntry.CONTENT_TYPE;
            case LABEL:
                return ToDoContract.TaskLabel.CONTENT_TYPE;
            case LABELS_WITH_TASK:
                return ToDoContract.TaskLabel.CONTENT_TYPE;
            case PRIORITY:
                return ToDoContract.TaskPriority.CONTENT_TYPE;
            case PRIORITY_WITH_ID:
                return ToDoContract.TaskPriority.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }
}
