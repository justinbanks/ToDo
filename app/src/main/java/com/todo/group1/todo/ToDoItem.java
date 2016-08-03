package com.todo.group1.todo;

import java.io.Serializable;

/**
 * Created by Justin Banks on 8/1/16.
 * This class implements a ToDoItem
 */

// Serializable is implemented here in order to pass the class between activities
public class ToDoItem implements Serializable{

    public String todo_title = "";
    public long date_in_ms = 0;
    public int due_day = 1;
    public int due_month = 1;
    public int due_year = 1970;
    public int due_hour = 12;
    public int due_minute = 0;
    public String priority = "None";
    public String details = "";
    public String label = "";
    public boolean is_complete = false;
    public boolean is_deleted = false;

    public ToDoItem(String todo_title, long date_in_ms, String priority, String details, String label,
                    boolean is_complete, boolean is_deleted){
        this.todo_title = todo_title;
        this.date_in_ms = date_in_ms;
        this.priority = priority;
        this.details = details;
        this.label = label;
        this.is_complete = is_complete;
        this.is_deleted = is_deleted;
    }
    public ToDoItem (String todo_title) {
        this.todo_title = todo_title;
    }

    // toString is overriden in order to use the item in an ArrayAdapter
    @Override
    public String toString() {
        return todo_title;
    }

}
