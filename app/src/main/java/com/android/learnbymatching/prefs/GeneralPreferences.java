package com.android.learnbymatching.prefs;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.android.learnbymatching.R;

/**
 * Created by Lenovo on 3.12.2016.
 *
 */

public class GeneralPreferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general);
    }
}
