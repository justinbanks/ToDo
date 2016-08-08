package com.todo.group1.todo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import android.widget.EditText;
import android.widget.ListView;

import com.todo.group1.todo.data.ToDoContract;

import java.util.Calendar;
import java.util.Date;

import static com.todo.group1.todo.data.ToDoContract.TaskEntry.COLUMN_TITLE;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int TASKLIST_LOADER = 0;
    private TaskListAdapter mTaskListAdapter;
    EditText input;

    @Override
    @SuppressWarnings("ConstantConditions")
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            startUpTestData();

            // mark first time ran
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }


        // Set up our tasklist
        Cursor taskCursor = this.getContentResolver().query(
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // set up the task list adapter
        mTaskListAdapter = new TaskListAdapter(this, taskCursor, 0);
        getSupportLoaderManager().initLoader(TASKLIST_LOADER, null, this);

        // attach the task list adapter to the list view
        ListView listview = (ListView) findViewById(R.id.listview_tasklist);
        listview.setAdapter(mTaskListAdapter);

        // This opens the detail view when a list item is clicked
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                int id = cursor.getInt(cursor.getColumnIndex(ToDoContract.TaskEntry._ID));

                if (cursor != null) {
                    Intent intent = new Intent (MainActivity.this, DetailActivity.class)
                            .setData(ToDoContract.TaskEntry.buildTaskWithId(Integer.toString(id)));
                    startActivity(intent);
                }
            }
        });

        // Set up the search bar
        input = (EditText) findViewById(R.id.inputSearch);
        listview.setTextFilterEnabled(true);

        // Search data change detection
//        Collections.sort(taskList, new Comparator<ToDoItem>() {
//            @Override
//            public int compare(ToDoItem t0, ToDoItem t1) {
//                return -t0.toString().compareToIgnoreCase(t1.toString());
//            }
//        });

        //updates listview when input is put into the search bar
        input = (EditText) findViewById(R.id.inputSearch);
        listview.setTextFilterEnabled(true);

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

        //hides soft keyboard when search is not focus
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                       public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus)
                                hideKeyboard(v);
                        }
                    });
    }

    public void startUpTestData() {
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
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_DELETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_LABEL_ID, validLabelId);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PARENT_TASK_ID, -1);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, 2);
        testValues.put(ToDoContract.TaskEntry.COLUMN_REMINDER_ADDED, 0);
        testValues.put(COLUMN_TITLE, "Buy a plant");
        this.getContentResolver().insert(ToDoContract.TaskEntry.CONTENT_URI, testValues);

        testValues = new ContentValues();
        testValues.put(ToDoContract.TaskEntry.COLUMN_CREATE_DATE, currentTime);
        testValues.put(ToDoContract.TaskEntry.COLUMN_DUE_DATE, currentTime);
        testValues.put(ToDoContract.TaskEntry.COLUMN_DETAIL, "this is important");
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_DELETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_LABEL_ID, validLabelId);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PARENT_TASK_ID, -1);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, 2);
        testValues.put(ToDoContract.TaskEntry.COLUMN_REMINDER_ADDED, 0);
        testValues.put(COLUMN_TITLE, "Catch some pokemon");
        this.getContentResolver().insert(ToDoContract.TaskEntry.CONTENT_URI, testValues);

        testValues = new ContentValues();
        testValues.put(ToDoContract.TaskEntry.COLUMN_CREATE_DATE, currentTime);
        testValues.put(ToDoContract.TaskEntry.COLUMN_DUE_DATE, currentTime);
        testValues.put(ToDoContract.TaskEntry.COLUMN_DETAIL, "this is important");
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_IS_DELETED, 0);
        testValues.put(ToDoContract.TaskEntry.COLUMN_LABEL_ID, validLabelId);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PARENT_TASK_ID, -1);
        testValues.put(ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, 2);
        testValues.put(ToDoContract.TaskEntry.COLUMN_REMINDER_ADDED, 0);
        testValues.put(COLUMN_TITLE, "HELP I'M STUCK IN A PHONE");
        this.getContentResolver().insert(ToDoContract.TaskEntry.CONTENT_URI, testValues);
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

    /**
     *
     * Loader Functions
     *
     *
     */
    @Override
    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {
        return new CursorLoader(this,
                ToDoContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mTaskListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mTaskListAdapter.swapCursor(null);
    }

    private void setCompletedTasksList() {
        // Build the Uri
        Uri uri = ToDoContract.TaskEntry.buildTaskMarkedComplete();

        // Query the database
        Cursor taskCursor = this.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        // Set up the task list adapter
        mTaskListAdapter = new TaskListAdapter(this, taskCursor, 0);

        // attach the task list adapter to the list view
        ListView listview = (ListView) findViewById(R.id.listview_tasklist);

        // Set the listview adapter
        listview.setAdapter(mTaskListAdapter);
    }

    private void setUpcomingTasksList() {
        // Build the Uri
        // generate a date
        Calendar cal = Calendar.getInstance();
        // get dates after tomorrow
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();
        Uri uri = ToDoContract.TaskEntry.buildTaskAfterDate(date);

        // Query the database

        // Convert the cursor to ToDoItem

        // Attach the task list adapter to the list view

        // Set up the task list adapter

        // Set the listview adapter

        // This opens the detail view when a list item is clicked
    }

    private void setHighPriorityTasksList() {
        // Build the Uri
        Uri uri = ToDoContract.TaskEntry.buildTaskWithPriority(getString(R.string.priority_high));

        // Query the database

        // Convert the cursor to ToDoItem

        // Attach the task list adapter to the list view

        // Set up the task list adapter

        // Set the listview adapter

        // This opens the detail view when a list item is clicked
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.completed_tasks) {
            setCompletedTasksList();
        } else if (id == R.id.upcoming_tasks) {
            setUpcomingTasksList();
        } else if (id == R.id.high_priority_tasks) {
            setHighPriorityTasksList();
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

    //hides soft keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //hides or shows the search bar when search icon is clicked
    public void hideSearch(MenuItem item) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (input.getVisibility() == View.GONE)
        {
            input.setVisibility(View.VISIBLE);
            input.requestFocus();
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        }
        else if (input.getVisibility() == View.VISIBLE)
            input.setVisibility(View.GONE);
    }

    //function to open the sort Dialog onClick of the sort button
    public void openDialog(MenuItem item) {
        DialogFragment newFragment = SortDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
