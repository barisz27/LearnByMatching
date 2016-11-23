package com.android.learnbymatching.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.M;

/*
 * Created by Lenovo on 17.11.2016.
 *
 */

public class NewMatchDialogFragment extends DialogFragment {

    private NewMatchUpdateListener newMatchUpdateListener;
    private static boolean mUpdate = false;

    // update ile kullanılacak metod
    public static NewMatchDialogFragment newInstance(int position, String first, String second) {
        Bundle bundle = new Bundle();
        bundle.putInt("selected_position", position);
        bundle.putString("first", first);
        bundle.putString("second", second);

        NewMatchDialogFragment newMatchDialogFragment = new NewMatchDialogFragment();
        newMatchDialogFragment.setArguments(bundle);

        mUpdate = true;
        return newMatchDialogFragment;
    }

    // yeni oluşturmak için
    public static NewMatchDialogFragment newInstance() {
        mUpdate = false;
        return new NewMatchDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View layout = inflater.inflate(R.layout.newmatch_dialog, null);

        Button bCancel, bSaveNew, bJustSave;
        final EditText etFirst, etSecond;

        bCancel = (Button) layout.findViewById(R.id.bCancel);
       // bSaveNew = (Button) layout.findViewById(R.id.bSaveNew);
        bJustSave = (Button) layout.findViewById(R.id.bJustSave);
        etFirst = (EditText) layout.findViewById(R.id.etFirst);
        etSecond = (EditText) layout.findViewById(R.id.etSecond);

        if (mUpdate) {
            String first = getArguments().getString("first");
            String second = getArguments().getString("second");
            etFirst.setText(first);
            etSecond.setText(second);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        bJustSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUpdate) {
                    newMatchUpdateListener.onCreateNew(etFirst.getText().toString(), etSecond.getText().toString());
                    dialog.dismiss();
                } else {
                    int selected_position = getArguments().getInt("selected_position");
                    saveToDatabase(selected_position, etFirst.getText().toString(), etSecond.getText().toString());
                    newMatchUpdateListener.onUpdate(selected_position, etFirst.getText().toString(), etSecond.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
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
        newMatchUpdateListener = (NewMatchUpdateListener) activity;
    }

    private void saveToDatabase(int position, String v1, String v2) {
        try {
            Matchings entry = new Matchings(getActivity());
            entry.open();
            entry.createEntry(getFullTime(), "a", getFullTime(), position, v1, position, v2);
            entry.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFullTime(){ // tarih + saat gösterir..
        String fullTime;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fullTime = dateFormat.format(c.getTime());

        return fullTime;
    }
}
