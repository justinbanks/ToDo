package com.todo.group1.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


/**
 * Created by Justin Banks on 7/25/16.
 * This file contains test cases related to the ToDoProvider class.
 */

public class TestProvider {

    private Context mContext = InstrumentationRegistry.getTargetContext();

    // This deletes all records from database tables using the Content Provider
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ToDoContract.TaskLabel.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Task table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ToDoContract.TaskLabel.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Label table during delete", 0, cursor.getCount());
        cursor.close();
    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testBasicTaskQuery() {

        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTaskEntryValues();
        long taskRowId = TestUtilities.insertTaskEntryValues(mContext);

        db.close();

        // Test content provider query
        Cursor taskCursor = mContext.getContentResolver().query(
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicTaskQuery", taskCursor, testValues);
    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testTasksWithPriorityQuery() {

        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTaskEntryValues();
        long taskRowId = TestUtilities.insertTaskEntryValues(mContext);

        db.close();

        // Test content provider query
        Cursor taskCursor = mContext.getContentResolver().query(
                ToDoContract.TaskEntry.buildTaskWithPriority("1"),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testTasksWithPriorityQuery", taskCursor, testValues);
    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testTasksBeforeDateQuery() {
        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTaskEntryValues();
        TestUtilities.insertTaskEntryValues(mContext);

        db.close();

        // generate a date
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2100);
        Date date = cal.getTime();

        // Test content provider query
        Cursor taskCursor = mContext.getContentResolver().query(
                ToDoContract.TaskEntry.buildTaskBeforeDate(date),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testTasksBeforeDateQuery", taskCursor, testValues);

    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testTasksWithLabelQuery() {
        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(ToDoContract.TaskEntry.TABLE_NAME, null, null);

        ContentValues testValues = TestUtilities.createTaskEntryValues();
        long taskRowId = TestUtilities.insertTaskEntryValues(mContext);

        db.close();

        // Test content provider query
        Cursor taskCursor = mContext.getContentResolver().query(
                ToDoContract.TaskEntry.buildTasksWithLabel("1"),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testTasksWithLabel", taskCursor, testValues);

    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testTasksMarkedComplete() {
        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTaskEntryValuesMarkedComplete();
        TestUtilities.insertTaskEntryValuesMarkedComplete(mContext);

        db.close();

        // Test content provider query
        Cursor taskCursor = mContext.getContentResolver().query(
                ToDoContract.TaskEntry.buildTaskMarkedComplete(),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testTasksMarkedComplete", taskCursor, testValues);

    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testBasicLabelQuery() {
        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createLabelValues();
        long labelRowId = TestUtilities.insertLabelEntryValues(mContext);

        db.close();

        // Test content provider query
        Cursor labelCursor = mContext.getContentResolver().query(
                ToDoContract.TaskLabel.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicLabelQuery", labelCursor, testValues);
    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testLabelByIdQuery() {
        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createLabelValues();
        db.close();

        // Test content provider query
        Cursor priorityCursor = mContext.getContentResolver().query(
                ToDoContract.TaskLabel.buildLabelWithId("1"), // uri
                null,   // projection
                null,   // selection
                null,   // selectionArgs
                null    // sortOrder
        );

        TestUtilities.validateCursor("testLabelByIdQuery", priorityCursor, testValues);
    }

    //  This test uses the database directly to insert and then uses the ContentProvider to
    //  read out the data.
    @org.junit.Test
    public void testPriorityByIdQuery() {
        // get a writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createPriorityValues();
        db.close();

        // Test content provider query
        Cursor priorityCursor = mContext.getContentResolver().query(
                ToDoContract.TaskPriority.buildPriorityWithId("2"), // uri
                null,   // projection
                null,   // selection
                null,   // selectionArgs
                null    // sortOrder
        );

        TestUtilities.validateCursor("testPriorityByIdQuery", priorityCursor, testValues);
    }
}