package com.todo.group1.todo.data;

import android.provider.BaseColumns;

/**
 * Created by Justin Banks on 7/23/16.
 * Defines table and column names for the to do database
 */
public class ToDoContract {

    // This class defines the contents of the task table
    public static final class TaskEntry implements BaseColumns {

        // define name of table
        public static final String TABLE_NAME = "task";

        // title and detail of task
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DETAIL = "detail";

        // date task is due
        public static final String COLUMN_DUE_DATE = "due_date";
        // date task was created
        public static final String COLUMN_CREATE_DATE = "create_date";

        // label(s) assigned to task
        public static final String COLUMN_LABEL_ID = "label_id";

        // priority assigned to task
        public static final String COLUMN_PRIORITY = "priority_id";

        // whether or not a reminder has been added
        public static final String COLUMN_REMINDER_ADDED = "reminder_added";
        // whether or not a task has been marked complete
        public static final String COLUMN_IS_COMPLETED = "is_complete";
        // whether or not a task has been marked deleted
        public static final String COLUMN_IS_DELETED = "is_deleted";

        // the parent task of a task, if it has one
        public static final String COLUMN_PARENT_TASK = "parent_task";
    }

    // This class defines the contents of the priority table
    public static final class TaskPriority implements BaseColumns {

        // define name of table
        public static final String TABLE_NAME = "priority";

        // define priority
        public static final String COLUMN_PRIORITY = "priority";
    }

    // This class defines the contents of the label table
    public static final class TaskLabel implements BaseColumns {

        // define name of table
        public static final String TABLE_NAME = "label";

        // define label
        public static final String COLUMN_LABEL = "label";
    }

}
