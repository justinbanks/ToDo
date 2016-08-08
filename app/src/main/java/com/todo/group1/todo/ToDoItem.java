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
    public Calendar calTime;
    public String priority = "None";
    public String details = "";
    public String label = "";
    public boolean isComplete = false;
    public boolean isDeleted = false;
    public int taskId = 0;

    public ToDoItem(String toDoTitle, long dateInMs, String priority, String details, String label,
                    boolean is_complete, boolean isDeleted){
        this.toDoTitle = (toDoTitle == null) ? "" : toDoTitle;
        this.dateInMs = dateInMs;
        this.priority = (priority == null) ? "" : priority;
        this.details = (details == null) ? "" : details;
        this.label = (label == null) ? "" : label;
        this.isComplete = is_complete;
        this.isDeleted = isDeleted;

        this.configureTime();
    }
    public ToDoItem () {}

    /**
     * ToString is overridden here in order to use a ToDoItem with an ArrayAdapter. This enables
     * a ToDoItem to be passed between activities.
     * @return The title of the task.
     */
    @Override
    public String toString() {
        return toDoTitle;
    }

    /**
     * Convert the long value datInMs, the amount of milliseconds since epoch, to a calendar value
     * and store it as a class variable.
     */
    public void configureTime() {
        calTime = Calendar.getInstance();
        calTime.setTimeInMillis(this.dateInMs);
    }
}
