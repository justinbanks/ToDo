package com.todo.group1.todo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Justin Banks on 8/1/16.
 * This class implements a ToDoItem
 */

// Serializable is implemented here in order to pass the class between activities
public class ToDoItem implements Serializable{

    public String toDoTitle = "";
    public long dateInMs = 0;
    public int dueDay = 1;
    public int dueMonth = 1;
    public int dueYear = 1970;
    public int dueHour = 12;
    public int dueMinute = 0;
    public Calendar calTime;
    public String priority = "None";
    public String details = "";
    public String label = "";
    public boolean isComplete = false;
    public boolean isDeleted = false;

    public ToDoItem(String toDoTitle, long dateInMs, String priority, String details, String label,
                    boolean is_complete, boolean isDeleted){
        this.toDoTitle = toDoTitle;
        this.dateInMs = dateInMs;
        this.priority = priority;
        this.details = details;
        this.label = label;
        this.isComplete = is_complete;
        this.isDeleted = isDeleted;

        this.configureTime();
    }
    public ToDoItem (String toDoTitle) {
        this.toDoTitle = toDoTitle;
    }

    // toString is overriden in order to use the item in an ArrayAdapter
    @Override
    public String toString() {
        return toDoTitle;
    }

    public void configureTime() {
        calTime = Calendar.getInstance();
        calTime.setTimeInMillis(this.dateInMs);
        dueDay = calTime.get(Calendar.DAY_OF_MONTH);
        dueMonth = calTime.get(Calendar.MONTH);
        dueYear = calTime.get(Calendar.YEAR);
        dueHour = calTime.get(Calendar.HOUR);
        dueMinute = calTime.get(Calendar.MINUTE);
    }
}
