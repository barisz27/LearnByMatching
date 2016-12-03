package com.android.learnbymatching.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;

/**
 * Created by Lenovo on 30.11.2016.
 *
 */

public class RenameProjectDialogFragment extends DialogFragment {

    private RenameUpdateListener listener;

    public static RenameProjectDialogFragment newInstance(String project_date, String project_name){
        Bundle bundle = new Bundle();
        bundle.putString("date", project_date);
        bundle.putString("name", project_name);

        RenameProjectDialogFragment renameProjectDialogFragment = new RenameProjectDialogFragment();
        renameProjectDialogFragment.setArguments(bundle);

        return renameProjectDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View layout = inflater.inflate(R.layout.rename_dialog, null);

        final String date = getArguments().getString("date");
        final String name = getArguments().getString("name");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        Button bRename = (Button) layout.findViewById(R.id.bRename);
        Button bCancelRename = (Button) layout.findViewById(R.id.bCancelRename);
        final EditText etRename = (EditText) layout.findViewById(R.id.etRename);
        etRename.setText(name);

        bRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRename(date, etRename.getText().toString());
                Matchings db = new Matchings(getActivity());
                db.updateProject(date, etRename.getText().toString());
                dialog.dismiss();
            }
        });

        bCancelRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RenameUpdateListener) activity;
    }

    public interface RenameUpdateListener {
        void onRename(String date, String newName);
    }
}
