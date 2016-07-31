package com.todo.group1.todo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Justin Banks on 7/24/16.
 * This class provides the content provider for the To Do application.
 */

@SuppressWarnings("ConstantConditions")
public class ToDoProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ToDoDbHelper mOpenHelper;

    // Uri Identification
    static final int TASKS = 100;
    static final int TASKS_WITH_PRIORITY = 101;
    static final int TASKS_AFTER_DATE = 102;
    static final int TASKS_MARKED_COMPLETE = 103;
    static final int TASKS_WITH_LABEL = 104;

    static final int LABEL = 300;
    static final int LABELS_WITH_TASK = 301;

    static final int PRIORITY = 500;
    static final int PRIORITY_WITH_ID = 501;

    // Functions for more complex queries

    private Cursor getTasksByPriorityId(Uri uri, String[] projection, String sortOrder) {
        return null;
    }

    private Cursor getTasksAfterDate(Uri uri, String[] projection, String sortOrder) {
        return null;
    }

    private Cursor getTasksMarkedComplete(Uri uri, String[] projection, String sortOrder) {
        return null;
    }

    private Cursor getTasksByLabelId(Uri uri, String[] projection, String sortOrder) {
        return null;
    }

    private Cursor getLabelsWithTask(Uri uri, String[] projection, String sortOrder) {
        return null;
    }

    static UriMatcher buildUriMatcher() {
        //The code passed into the constructor represents the code to return for the root URI
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ToDoContract.CONTENT_AUTHORITY;

        // Use the addURI function to match each of the types
        matcher.addURI(authority, ToDoContract.PATH_TASK, TASKS);
        matcher.addURI(authority, ToDoContract.PATH_TASK + "/priority/#", TASKS_WITH_PRIORITY);
        matcher.addURI(authority, ToDoContract.PATH_TASK + "/date/#", TASKS_AFTER_DATE);
        matcher.addURI(authority, ToDoContract.PATH_TASK + "/complete", TASKS_MARKED_COMPLETE);
        matcher.addURI(authority, ToDoContract.PATH_TASK + "/label/#", TASKS_WITH_LABEL);

        matcher.addURI(authority, ToDoContract.PATH_LABEL, LABEL);
        matcher.addURI(authority, ToDoContract.PATH_LABEL + "/task/#", LABELS_WITH_TASK);

        matcher.addURI(authority, ToDoContract.PATH_PRIORITY, PRIORITY);
        matcher.addURI(authority, ToDoContract.PATH_PRIORITY + "/#", PRIORITY_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ToDoDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch(sUriMatcher.match(uri)) {
            case TASKS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ToDoContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TASKS_WITH_PRIORITY:
            {
                retCursor = getTasksByPriorityId(uri, projection, sortOrder);
                break;
            }
            case TASKS_AFTER_DATE:
            {
                retCursor = getTasksAfterDate(uri, projection, sortOrder);
                break;
            }
            case TASKS_WITH_LABEL:
            {
                retCursor = getTasksByLabelId(uri, projection, sortOrder);
                break;
            }
            case TASKS_MARKED_COMPLETE:
            {
                retCursor = getTasksMarkedComplete(uri, projection, sortOrder);
                break;
            }
            case LABEL:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ToDoContract.TaskLabel.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LABELS_WITH_TASK:
            {
                retCursor = getLabelsWithTask(uri, projection, sortOrder);
                break;
            }
            case PRIORITY:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ToDoContract.TaskPriority.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PRIORITY_WITH_ID:
            {
                selection = "_id = ?";
                selectionArgs = new String [] {ToDoContract.TaskPriority.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ToDoContract.TaskPriority.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }
}
