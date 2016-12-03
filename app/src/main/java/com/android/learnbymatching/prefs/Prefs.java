package com.android.learnbymatching.prefs;

import android.preference.PreferenceActivity;
import com.android.learnbymatching.R;
import java.util.List;

/**
 * Created by Lenovo on 3.12.2016.
 *
 */

public class Prefs extends PreferenceActivity {

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return  GeneralPreferences.class.getName().equals(fragmentName);

    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preferences, target);
    }
}
