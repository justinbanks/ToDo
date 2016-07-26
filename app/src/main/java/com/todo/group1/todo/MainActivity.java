package com.todo.group1.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText inputSearch;
    private ArrayAdapter<String> mTaskListAdapter;


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

        // Create some dummy data for the ListView.  Here's a sample tasklist
        String[] data = {
                "Buy a new tie",
                "Walk John's dog",
                "Find a good eggplant recipe",
                "Get a job",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7"
        };
        List<String> taskList = new ArrayList<>(Arrays.asList(data));


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

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        listview.setTextFilterEnabled(true);

        inputSearch.addTextChangedListener(new TextWatcher() {
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


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String tasklist = mTaskListAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, tasklist);
                startActivity(intent);
            }
        });

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
