package com.todo.group1.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.test.InstrumentationRegistry;

import com.todo.group1.todo.R;
import com.todo.group1.todo.util.PollingCheck;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Justin Banks on 7/25/16.
 * This file contains methods that aid in unit testing.
 */
public class TestUtilities {

    private static long currentTime = System.currentTimeMillis();
    private static Context mContext = InstrumentationRegistry.getTargetContext();

    // This function validates an actual database record against the expected record.
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            if (!valueCursor.getString(idx).equals("")) {
                assertEquals("Value '" + entry.getValue().toString() +
                        "' did not match the expected value '" +
                        expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
            }
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    // This function generates a ContentValue task entry
    static ContentValues createTaskEntryValues() {
        ContentValues taskValues = new ContentValues();
        taskValues.put(ToDoContract.TaskEntry.COLUMN_CREATE_DATE, currentTime);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_DUE_DATE, currentTime);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_DETAIL, "this is important");
        taskValues.put(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED, 0);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_IS_DELETED, 0);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_LABEL_ID, 1);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_PARENT_TASK_ID, -1);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, 1);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_REMINDER_ADDED, 0);
        taskValues.put(ToDoContract.TaskEntry.COLUMN_TITLE, "Buy a plant");

        return taskValues;
    }

    static ContentValues createLabelValues() {
        ContentValues labelValues = new ContentValues();
        labelValues.put(ToDoContract.TaskLabel.COLUMN_LABEL, "test_label");
        return labelValues;
    }

    static ContentValues createPriorityValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(ToDoContract.TaskPriority.COLUMN_PRIORITY,
                mContext.getString(R.string.priority_low));
        return testValues;
    }

    static long insertTaskEntryValues (Context context) {
        // insert our test records into the database
        ToDoDbHelper dbHelper = new ToDoDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTaskEntryValues();

        long taskRowId;
        taskRowId = db.insert(ToDoContract.TaskEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Task Entry Values", taskRowId != -1);

        return taskRowId;
    }

    static long insertLabelEntryValues (Context context) {
        // insert our test records into the database
        ToDoDbHelper dbHelper = new ToDoDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createLabelValues();

        long labelRowId;
        labelRowId = db.insert(ToDoContract.TaskLabel.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Label Values", labelRowId != -1);

        return labelRowId;
    }

    // This tests the ContentObserver callbacks
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}


