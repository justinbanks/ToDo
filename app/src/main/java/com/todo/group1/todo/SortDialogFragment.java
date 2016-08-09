package com.todo.group1.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.todo.group1.todo.data.ToDoContract;

//Dialog Fragment class to build sort Dialog
public class SortDialogFragment extends DialogFragment {

    public static SortDialogFragment newInstance() {
        SortDialogFragment frag = new SortDialogFragment();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.toolbar_sort_title)
                .setItems(R.array.pref_sort_list_titles, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if( which == 0)
                        {
                            Log.d("debug", "priority");
                            MainActivity.sortOrder = ToDoContract.TaskEntry.COLUMN_PRIORITY_ID + " ASC";
                        }
                        else if( which == 1)
                        {
                            defaultSort(0);
                        }
                        else if( which == 2)
                        {
                            defaultSort(1);
                        }
                    }
                });
        return builder.create();
    }

    public String defaultSort(int which)
    {

        switch (which){
            case 1:
                return ToDoContract.TaskEntry.COLUMN_PRIORITY_ID + " ASC";
            case 0:
                return ToDoContract.TaskEntry.COLUMN_DUE_DATE + " ASC";
            case -1:
                return ToDoContract.TaskEntry.COLUMN_TITLE + " ASC";

            default:
                return ToDoContract.TaskEntry.COLUMN_TITLE + " ASC";
        }
    }
}