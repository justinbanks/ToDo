package com.todo.group1.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Justin Banks on 7/25/16.
 * This file contains test cases pertaining to the ToDoDbHelper and ToDoContract classes.
 */

public class TestDb {

    public static final String LOG_TAG = TestDb.class.getSimpleName();
    private Context mContext = null;


    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(ToDoDbHelper.DATABASE_NAME);
    }

    @org.junit.Before
    public void setUp() {
        deleteTheDatabase();
    }

    @org.junit.Test
    public void testCreateDb() throws Throwable {
        // build a hashset of all table names we wish to look for
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(ToDoContract.TaskEntry.TABLE_NAME);
        tableNameHashSet.add(ToDoContract.TaskPriority.TABLE_NAME);
        tableNameHashSet.add(ToDoContract.TaskLabel.TABLE_NAME);

        mContext.deleteDatabase(ToDoDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new ToDoDbHelper(this.mContext).getWritableDatabase();

        // make sure the database is open
        assertEquals(true, db.isOpen());

        // make sure we've created the tables we want
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);
        assertTrue("Error: Database not created correctly", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that the database doesn't contain the desired tables
        assertTrue("Error: Your database was created without the task, priority, and label tabels",
                tableNameHashSet.isEmpty());

        // check if tables contain the correct columns
        c = db.rawQuery("PRAGMA table_info(" + ToDoContract.TaskEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> taskEntryColumnHashset = new HashSet<>();
        taskEntryColumnHashset.add(ToDoContract.TaskEntry._ID);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_CREATE_DATE);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_DETAIL);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_DUE_DATE);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_IS_DELETED);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_LABEL_ID);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_PARENT_TASK_ID);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_PRIORITY_ID);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_TITLE);
        taskEntryColumnHashset.add(ToDoContract.TaskEntry.COLUMN_TITLE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            taskEntryColumnHashset.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required task
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required task entry columns",
                taskEntryColumnHashset.isEmpty());
        db.close();
        c.close();
    }

    public void testTaskTable() {
        // get reference to writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // create task values
        ContentValues toDoValues = TestUtilities.createTaskEntryValues();

        // insert to do values into database and receive a row ID
        long taskRowId = db.insert(ToDoContract.TaskEntry.TABLE_NAME, null, toDoValues);
        assertTrue(taskRowId != -1);

        // Query the database and receive a Cursor back
        Cursor toDoCursor = db.query(
                ToDoContract.TaskEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from task query", toDoCursor.moveToFirst() );

        // validate the location query
        TestUtilities.validateCurrentRecord("testInsertReadDb task failed to validate",
                toDoCursor, toDoValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from task query",
                toDoCursor.moveToNext() );

        // close cursor and database
        toDoCursor.close();
        dbHelper.close();
    }

    public void testPriorityTable() {
        // make sure we're working with a fresh writable database
        mContext.deleteDatabase(ToDoDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new ToDoDbHelper(this.mContext).getWritableDatabase();

        // make sure the database is open
        assertEquals(true, db.isOpen());

        // check if table contains the correct columns
        Cursor c = db.rawQuery("PRAGMA table_info(" + ToDoContract.TaskEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> priorityColumnHashset = new HashSet<>();
        priorityColumnHashset.add(ToDoContract.TaskPriority._ID);
        priorityColumnHashset.add(ToDoContract.TaskPriority.COLUMN_PRIORITY);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            priorityColumnHashset.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required task
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required task entry columns",
                priorityColumnHashset.isEmpty());
        db.close();
        c.close();
    }

    public void testLabelTable() {
        // get reference to writable database
        ToDoDbHelper dbHelper = new ToDoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // create task values
        ContentValues labelValues = TestUtilities.createLabelValues();

        // insert to do values into database and receive a row ID
        long labelRowId = db.insert(ToDoContract.TaskLabel.TABLE_NAME, null, labelValues);
        assertTrue(labelRowId != -1);

        // Query the database and receive a Cursor back
        Cursor labelCursor = db.query(
                ToDoContract.TaskLabel.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from label query", labelCursor.moveToFirst() );

        // validate the location query
        TestUtilities.validateCurrentRecord("testInsertReadDb label failed to validate",
                labelCursor, labelValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from label query",
                labelCursor.moveToNext() );

        // close cursor and database
        labelCursor.close();
        dbHelper.close();
    }
}