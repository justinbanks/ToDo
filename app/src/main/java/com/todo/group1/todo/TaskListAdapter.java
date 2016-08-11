package com.todo.group1.todo;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todo.group1.todo.data.ToDoContract;

/**
 * {@link TaskListAdapter} exposes a list of tasks
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class TaskListAdapter extends CursorAdapter {

    public TaskListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Convert a cursor to String.
     * @param cursor
     * @return
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        int idx_title = cursor.getColumnIndex(ToDoContract.TaskEntry.COLUMN_TITLE);
        return cursor.getString(idx_title);
    }

    /**
     * Create a new view, they are reused as needed.
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
    }

    /**
     * Fill in the views with the contents of the cursor.
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}