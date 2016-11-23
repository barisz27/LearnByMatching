package com.android.learnbymatching.dialog;

/**
 * Created by Lenovo on 17.11.2016.
 *
 */

public interface NewMatchUpdateListener {
    void onUpdate(int position, String first, String second);

    void onCreateNew(String first, String second);
}
