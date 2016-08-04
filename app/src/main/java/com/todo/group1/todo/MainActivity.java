package com.todo.group1.todo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.todo.group1.todo.data.ToDoContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayAdapter<ToDoItem> mTaskListAdapter;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // We need to request write permissions at runtime for API > 23
        final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        this.getContentResolver().delete(
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null
        );

        this.getContentResolver().delete(
                ToDoContract.TaskLabel.CONTENT_URI,
                null,
                null
        );

        ContentValues labelValues = new ContentValues();
        labelValues.put(ToDoContract.TaskLabel.COLUMN_LABEL, "YEEEESSS");
        this.getContentResolver().insert(ToDoContract.TaskLabel.CONTENT_URI, labelValues);

        Cursor labelCursor = this.getContentResolver().query(
                ToDoContract.TaskLabel.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        int validLabelId = -1;
        while (labelCursor.moveToNext()) {
            validLabelId = labelCursor.getInt(
                    labelCursor.getColumnIndex(ToDoContract.TaskLabel._ID));
        }

        // generating some test data
        long currentTime = System.currentTimeMillis() + 1000000;
        ContentValues testValues = new ContentValues();
        testValues.put(ToDoContract.TaskEntry.COLUMN_CREATE_DATE, currentTime);
        testValues.put(ToDoContract.TaskEntry.COLUMN_DUE_DATE, currentTime);
        testValues.put(ToDoContract.TaskEntry.COLUMN_DETAIL, "this is important");
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED, 1);
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_DELETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_LABEL_ID, validLabelId);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PARENT_TASK_ID, -1);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, 2);
        testValues.put(ToDoContract.TaskEntry.COLUMN_REMINDER_ADDED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_TITLE, "Buy a plant");

        this.getContentResolver().insert(ToDoContract.TaskEntry.CONTENT_URI, testValues);

        // Set up our tasklist
        Cursor taskCursor = getAllTasks();
        List<ToDoItem> taskList = retrieveTasksFromCursor(taskCursor);
        taskCursor.close();

        // set up the task list adapter
        mTaskListAdapter =
                new ArrayAdapter<>(
                        this, // The current context (this activity)
                        R.layout.list_item_task, // The name of the layout ID.
                        R.id.list_item_task_textview, // The ID of the textview to populate.
                        taskList);

        // attach the task list adapter to the list view
        ListView listview = (ListView) findViewById(R.id.listview_tasklist);
        listview.setAdapter(mTaskListAdapter);

        // Set up the search bar
        input = (EditText) findViewById(R.id.inputSearch);
        listview.setTextFilterEnabled(true);

        // Search data change detection
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int i, int i1, int i2) {
                MainActivity.this.mTaskListAdapter.getFilter().filter(arg0);
            }

            @Override
            public void onTextChanged(CharSequence arg0, int i, int i1, int i2) {
                MainActivity.this.mTaskListAdapter.getFilter().filter(arg0);

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                MainActivity.this.mTaskListAdapter.getFilter().filter(arg0);

            }
        });

        // This opens the detail view when a list item is clicked
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ToDoItem tasklist = mTaskListAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                        .putExtra("ToDoItem", tasklist);
                startActivity(intent);
            }
        });

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                       public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                        hideKeyboard(v);
                                    }
                            }
                    });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.completed_tasks) {
            // Handle the completed tasks action
        } else if (id == R.id.upcoming_tasks) {
            // Handle the upcoming tasks action
        } else if (id == R.id.high_priority_tasks) {
            // Handle the high priority tasks action
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.extra_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideSearch(MenuItem item) {
        if(input.getVisibility() == View.GONE)
            input.setVisibility(View.VISIBLE);
        else if(input.getVisibility() == View.VISIBLE)
            input.setVisibility(View.GONE);
    }

    public Cursor getAllTasks() {
        // Test content provider query
        Cursor taskCursor = this.getContentResolver().query(
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        return taskCursor;
    }

    public List<ToDoItem> retrieveTasksFromCursor(Cursor taskCursor) {
        List<ToDoItem> taskList = new ArrayList<>();
        while (taskCursor.moveToNext()) {
            String title = taskCursor.getString(
                    taskCursor.getColumnIndex(ToDoContract.TaskEntry.COLUMN_TITLE));
            String details = taskCursor.getString(taskCursor.getColumnIndex(
                    ToDoContract.TaskEntry.COLUMN_DETAIL));
            String label = taskCursor.getString(
                    taskCursor.getColumnIndex(ToDoContract.TaskLabel.COLUMN_LABEL));
            long date = taskCursor.getLong(
                    taskCursor.getColumnIndex(ToDoContract.TaskEntry.COLUMN_DUE_DATE));
            String priority = taskCursor.getString(
                    taskCursor.getColumnIndex(ToDoContract.TaskPriority.COLUMN_PRIORITY));
            int complete = taskCursor.getInt(
                    taskCursor.getColumnIndex(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED));
            boolean is_complete = (complete != 0);
            int delete = taskCursor.getInt(
                    taskCursor.getColumnIndex(ToDoContract.TaskEntry.COLUMN_IS_DELETED));
            boolean is_deleted = (delete != 0);

            ToDoItem item = new ToDoItem(title, date, priority, details, label,
                    is_complete, is_deleted);
            taskList.add(item);
        }
        return taskList;
    }
}
