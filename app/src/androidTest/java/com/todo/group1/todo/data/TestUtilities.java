package com.todo.group1.todo.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Justin Banks on 7/25/16.
 * This file contains methods that aid in unit testing.
 */
public class TestUtilities {

    private static long currentTime = System.currentTimeMillis();

    // This function validates an actual database record against the expected record.
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
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
}
