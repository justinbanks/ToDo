package com.todo.group1.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.todo.group1.todo.data.ToDoContract;

/**
 * DialogFragment that allows the user to select a sorting method.
 */
public class SortDialogFragment extends DialogFragment {

    public static SortDialogFragment newInstance() {
        SortDialogFragment frag = new SortDialogFragment();
        return frag;
    }

    public interface onSortSelectListener {
        void DoneSort(); //add parameters if needed
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String tempSort = MainActivity.sortOrder;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.toolbar_sort_title)
                .setSingleChoiceItems(R.array.pref_sort_list_titles, 3, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                    }
                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        if(selectedPosition == 0) {
                            MainActivity.sortOrder = ToDoContract.TaskEntry.COLUMN_PRIORITY_ID + " ASC";
                            //Log.d("checked pos", "priority checked");
                        }
                        else if(selectedPosition == 1) {
                            MainActivity.sortOrder = ToDoContract.TaskEntry.COLUMN_DUE_DATE + " ASC";
                            //Log.d("checked pos", "time checked");
                        }
                        else if(selectedPosition == 2) {
                            MainActivity.sortOrder = ToDoContract.TaskEntry.COLUMN_TITLE + " ASC";
                            //Log.d("checked pos", "alph checked");
                        }
                        onSortSelectListener activity = (onSortSelectListener) getActivity();
                        activity.DoneSort(); //make sure you add the same parameters as you did above
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen
                        MainActivity.sortOrder = tempSort;
                        onSortSelectListener activity = (onSortSelectListener) getActivity();
                        activity.DoneSort(); //make sure you add the same parameters as you did above
                    }
                });
        return builder.create();
    }
}