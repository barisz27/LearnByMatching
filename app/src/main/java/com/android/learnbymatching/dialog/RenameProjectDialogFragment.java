package com.android.learnbymatching.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.learnbymatching.R;

/**
 * Created by Lenovo on 30.11.2016.
 *
 */

public class RenameProjectDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View layout = inflater.inflate(R.layout.rename_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        Button bRename = (Button) layout.findViewById(R.id.bRename);
        Button bCancelRename = (Button) layout.findViewById(R.id.bCancelRename);
        EditText etRename = (EditText) layout.findViewById(R.id.etRename);

        bRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        bCancelRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return dialog;
    }
}
