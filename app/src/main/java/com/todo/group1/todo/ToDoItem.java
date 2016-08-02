package com.todo.group1.todo;

import java.io.Serializable;

/**
 * Created by Justin Banks on 8/1/16.
 * This class implements a ToDoItem
 */

// Serializable is implemented here in order to pass the class between activities
public class ToDoItem implements Serializable{
    public ToDoItem(String todo_title, int due_day, int due_month, int due_year, int due_hour,
                    int due_minute, String priority, String details, String label,
                    boolean is_complete, boolean is_deleted){
        this.todo_title = todo_title;
        this.due_day = due_day;
        this.due_month = due_month;
        this.due_year = due_year;
        this.due_hour = due_hour;
        this.due_minute = due_minute;
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

    public String todo_title;
    public int due_day;
    public int due_month;
    public int due_year;
    public int due_hour;
    public int due_minute;
    public String priority;
    public String details;
    public String label;
    public boolean is_complete;
    public boolean is_deleted;
}
