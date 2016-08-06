package com.todo.group1.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.todo.group1.todo.data.ToDoContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.todo.group1.todo.data.ToDoContract.TaskEntry.COLUMN_TITLE;

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


    public static class DetailFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        private static final int DETAIL_LOADER = 0;

        private View rootview;
        private ToDoItem toDoItem;
        private boolean isNew;

        // find our UI elements
        private EditText editTitle;
        private EditText editLabel;
        private EditText editDetails;
        private Button dateButton;
        private Button timeButton;
        private Spinner prioritySpinner;
        private Button addReminderButton;

        // Maps priority IDs to strings
        private Map<Integer, String> priorityMap;

        // ContentValues that will later be inserted into db
        ContentValues values = new ContentValues();

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        /**
         * Tasks to be done on creation of the view.
         * @param inflater The inflater to be used.
         * @param container A ViewGroup.
         * @param savedInstanceState The current instance state.
         * @return The rootview.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootview = inflater.inflate(R.layout.fragment_detail, container, false);
            return rootview;
        }

        /**
         * Inflate the options menu.
         * @param menu the menu to be inflated.
         * @param inflater the inflater to be used.
         */
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.detailfragment, menu);

            MenuItem menuItemDelete = menu.findItem(R.id.action_delete);
            MenuItem menuItemAdd = menu.findItem(R.id.action_complete);
        }

        /**
         * Configure the time selector. Set the onClickListener to a TimePickerFragment.
         * And configure the default time shown.
         */
        public void setUpTimeSelector() {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat smp = new SimpleDateFormat("h:mm a", Locale.US);
            timeButton.setText(smp.format(cal.getTime()));
            timeButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "timePicker");
                }
            });
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }

            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (!data.moveToFirst()) { return; }

            String title = data.getString(data.getColumnIndex(COLUMN_TITLE));
            String details = data.getString(data.getColumnIndex(ToDoContract.TaskEntry.COLUMN_DETAIL));
            String label = data.getString(data.getColumnIndex(ToDoContract.TaskLabel.COLUMN_LABEL));
            long date = data.getLong(data.getColumnIndex(ToDoContract.TaskEntry.COLUMN_DUE_DATE));
            String priority = data.getString(data.getColumnIndex(ToDoContract.TaskPriority.COLUMN_PRIORITY));
            int complete = data.getInt(data.getColumnIndex(ToDoContract.TaskEntry.COLUMN_IS_COMPLETED));
            boolean is_complete = (complete != 0);
            int delete = data.getInt(data.getColumnIndex(ToDoContract.TaskEntry.COLUMN_IS_DELETED));
            boolean is_deleted = (delete != 0);

            toDoItem = new ToDoItem(title, date, priority, details, label, is_complete, is_deleted);
            toDoItem.taskId = data.getInt(data.getColumnIndex(ToDoContract.TaskEntry._ID));

            // find our UI elements
            editTitle = (EditText) rootview.findViewById(R.id.edit_title);
            editLabel = (EditText) rootview.findViewById(R.id.edit_label);
            editDetails = (EditText) rootview.findViewById(R.id.edit_details);
            dateButton = (Button) rootview.findViewById(R.id.date_selector);
            timeButton = (Button) rootview.findViewById(R.id.time_selector);
            prioritySpinner = (Spinner) rootview.findViewById(R.id.priority_spinner);
            addReminderButton = (Button) rootview.findViewById(R.id.add_reminder_button);

            setUpTimeSelector();
            setUpDateSelector();
            setUpPrioritySpinner();
            setUpAddReminderButton();

            getPriorityIds();
            populateFields(toDoItem);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) { }

        /**
         * Configure the date selector. Set the onlickListener to the DatePickerFragment
         * and configure the default date shown.
         */
        public void setUpDateSelector() {
            // set up the date
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat smp = new SimpleDateFormat("E, MMM d", Locale.US);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dateButton.setText(smp.format(cal.getTime()));
            dateButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            });
        }

        /**
         * Configure the spinner that shows priorities.
         */
        public void setUpPrioritySpinner(){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.detail_priority_values, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            prioritySpinner.setAdapter(adapter);

            prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Uri uri = ToDoContract.TaskEntry.CONTENT_URI;
                    String valueString = parentView.getItemAtPosition(position).toString();
                    String priorityId = "";

                    // Only update if it's a freshly changed value
                    if (!valueString.equals(toDoItem.priority)) {
                        for (Map.Entry<Integer, String> e : priorityMap.entrySet()) {
                            int key = e.getKey();
                            String value = e.getValue();
                            if (value.equals(valueString))
                                priorityId = Integer.toString(key);
                        }
                        if (isNew)
                            insertTask(uri, ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, priorityId);
                        else
                            updateTask(uri, ToDoContract.TaskEntry.COLUMN_PRIORITY_ID, priorityId);
                    }

                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) { }
            });

        }

        /**
         * Configure the reminder button to send out a calendar intent.
         */
        public void setUpAddReminderButton(){
            addReminderButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_EDIT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, toDoItem.calTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, toDoItem.calTime.getTimeInMillis()+60*60*1000)
                            .putExtra(CalendarContract.Events.TITLE, toDoItem.toDoTitle)
                            .putExtra(CalendarContract.Events.DESCRIPTION, toDoItem.details);
                    startActivity(intent);
                }
            });
        }

        /**
         * Populates the fields of a the activity if an intent was passed.
         */
        private void populateFields(ToDoItem item) {
            if (!item.toDoTitle.equals(""))
                editTitle.setText(item.toDoTitle);
            if (!item.label.equals(""))
                editLabel.setText(item.label);
            if (!item.details.equals(""))
                editDetails.setText(item.details);
            if (!item.priority.equals("")) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.detail_priority_values, android.R.layout.simple_spinner_item);
                prioritySpinner.setSelection(adapter.getPosition(item.priority));
            }
            if (item.dateInMs != 0) {
                item.configureTime();
                SimpleDateFormat smp = new SimpleDateFormat("E, MMM d", Locale.US);
                dateButton.setText(smp.format(item.calTime.getTime()));

                smp = new SimpleDateFormat("h:mm a", Locale.US);
                timeButton.setText(smp.format(item.calTime.getTime()));
            }
        }

        private void getPriorityIds() {
            Cursor priorityCursor = getContext().getContentResolver().query(
                    ToDoContract.TaskPriority.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            priorityMap = new HashMap<>();
            while (priorityCursor.moveToNext()) {
                String prString = priorityCursor.getString(
                        priorityCursor.getColumnIndex(ToDoContract.TaskPriority.COLUMN_PRIORITY));
                int prId = priorityCursor.getInt(
                        priorityCursor.getColumnIndex(ToDoContract.TaskPriority._ID));
                priorityMap.put(prId, prString);
            }
        }

        private void updateTask(Uri uri, String columnName, String value) {
            // Generate ContentValues and insert
            ContentValues mValues = new ContentValues();
            mValues.put(columnName, value);

            String wClause = "_id = ?";
            String [] id = { Integer.toString(toDoItem.taskId) };

            this.getContext().getContentResolver().update(
                    uri,
                    mValues,
                    wClause,
                    id
            );
        }

        private void insertTask(Uri uri, String columnName, String value) {
            // Generate ContentValues and insert
            // Get taskId
            // Set to do item's task id
            // Set isNew to F
        }
    }
}
