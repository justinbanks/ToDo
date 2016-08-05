package com.todo.group1.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

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
                    }
                });
        return builder.create();
    }
}