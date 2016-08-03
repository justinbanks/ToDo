package com.todo.group1.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private View rootview;
        private ToDoItem item;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootview = inflater.inflate(R.layout.fragment_detail, container, false);
            setUpTimeSelector();
            setUpDateSelector();
            setUpPrioritySpinner();

            // If the view was called with an intent, we want to populate the fields
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("ToDoItem")) {
                item = (ToDoItem) intent.getSerializableExtra("ToDoItem");
                populateFields(item);
            }

            return rootview;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.detailfragment, menu);

            MenuItem menuItemDelete = menu.findItem(R.id.action_delete);
            MenuItem menuItemAdd = menu.findItem(R.id.action_complete);
        }

        public void setUpTimeSelector() {
            Button time_button = (Button) rootview.findViewById(R.id.time_selector);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat smp = new SimpleDateFormat("h:mm a", Locale.US);
            time_button.setText(smp.format(cal.getTime()));

            time_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "timePicker");
                }
            });
        }

        public void setUpDateSelector() {
            Button date_button = (Button) rootview.findViewById(R.id.date_selector);
            // set up the date
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat smp = new SimpleDateFormat("E, MMM d", Locale.US);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            date_button.setText(smp.format(cal.getTime()));

            date_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            });
        }

        public void setUpPrioritySpinner(){
            Spinner spinner = (Spinner) rootview.findViewById(R.id.priority_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.detail_priority_values, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);
        }

        private void populateFields(ToDoItem item) {
            EditText editTitle = (EditText) rootview.findViewById(R.id.edit_title);
            EditText editLabel = (EditText) rootview.findViewById(R.id.edit_label);
            EditText editDetails = (EditText) rootview.findViewById(R.id.edit_details);
            Button dateButton = (Button) rootview.findViewById(R.id.date_selector);
            Button timeButton = (Button) rootview.findViewById(R.id.time_selector);
            Spinner prioritySpinner = (Spinner) rootview.findViewById(R.id.priority_spinner);

            if (!item.todo_title.equals(""))
                editTitle.setText(item.todo_title);
            if (!item.label.equals(""))
                editLabel.setText(item.label);
            if (!item.details.equals(""))
                editDetails.setText(item.details);
            if (!item.priority.equals("")) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.detail_priority_values, android.R.layout.simple_spinner_item);
                prioritySpinner.setSelection(adapter.getPosition(item.priority));
            }
        }
    }
}
