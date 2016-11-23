package com.android.learnbymatching.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.learnbymatching.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.id.list;
import static android.view.View.Z;

public class GameActivity extends AppCompatActivity {

    private ArrayList<String> firstArray, secondArray;
    private String[] chosed;
    private int failCount; // toplam hatalı eşleştirme sayısı

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        failCount = 0;

        firstArray = getIntent().getExtras().getStringArrayList("first");
        secondArray = getIntent().getExtras().getStringArrayList("second");

        chosed = new String[2];
        final boolean[] isChosed = {false};
        int[] randomArrangement = generateRandomArray(firstArray.size());
        final List<Button> buttonList = new ArrayList<>();

        for (int i = 0; i < firstArray.size(); i++) {
            String text = firstArray.get(i);
            final Button myButton = new Button(GameActivity.this);
            myButton.setTag("Group1" + i);
            myButton.setText(text);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isChosed[0]) {
                        chosed[0] = myButton.getText().toString();
                        myButton.setEnabled(false);
                        isChosed[0] = true;
                    }
                }
            });
            buttonList.add(myButton);
        }

        for (int i = 0; i < firstArray.size(); i++)
        {
            LinearLayout ll = (LinearLayout) findViewById(R.id.llGroup1);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(buttonList.get(randomArrangement[i]), llParams);
        }

        buttonList.clear();

        for (int i = 0; i < secondArray.size(); i++) {
            String text = secondArray.get(i);
            final Button myButton = new Button(GameActivity.this);
            myButton.setTag("Group2" + i);
            myButton.setText(text);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chosed[1] = myButton.getText().toString();
                    // int indexFirst = firstArray.indexOf(chosed[0]);
                    if (chosed[0] != null) {
                        if (getStringPos(chosed[0], 1) == getStringPos(chosed[1], 2)) {
                            isChosed[0] = false;
                            Toast.makeText(GameActivity.this, "Doğru", Toast.LENGTH_SHORT).show();
                            myButton.setEnabled(false);
                            clearChosedArray();
                        } else {
                            failCount++;
                            isChosed[0] = false;
                            Toast.makeText(GameActivity.this, "Yanlış", Toast.LENGTH_SHORT).show();
                            List<View> bArray = findViewWithTagRecursively((ViewGroup) findViewById(R.id.llGroup1), "Group1" + getStringPos(chosed[0], 1));
                            Button b = (Button) bArray.get(0);
                            b.setEnabled(true);
                            clearChosedArray();
                        }
                    }

                    if (isGameFinish(firstArray.size())) {
                        new AlertDialog.Builder(GameActivity.this)
                                .setMessage("Oyun Bitti\n" + failCount + " defa yanıldınız")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }
            });

            buttonList.add(myButton);
        }

        randomArrangement = generateRandomArray(secondArray.size());

        for (int i = 0; i < secondArray.size(); i++)
        {
            LinearLayout ll = (LinearLayout) findViewById(R.id.llGroup2);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(buttonList.get(randomArrangement[i]), llParams);
        }
    }

    private int getStringPos(String category, int which) {
        if (which == 1)
            return firstArray.indexOf(category);
        else if (which == 2)
            return secondArray.indexOf(category);
        else
            return -1;
    }

    public static List<View> findViewWithTagRecursively(ViewGroup root, Object tag) {
        List<View> allViews = new ArrayList<>();

        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = root.getChildAt(i);

            if (childView instanceof ViewGroup) {
                allViews.addAll(findViewWithTagRecursively((ViewGroup) childView, tag));
            } else {
                final Object tagView = childView.getTag();
                if (tagView != null && tagView.equals(tag))
                    allViews.add(childView);
            }
        }

        return allViews;
    }

    private boolean isGameFinish(int lenght) {

        List<View> list, list1;

        View[] ourViewArray = new View[lenght];

        for (int i = 0; i < lenght; i++) {
            list = findViewWithTagRecursively((ViewGroup) findViewById(R.id.llGroup1), "Group1" + i);
            ourViewArray[i] = list.get(0);
            if (ourViewArray[i].isEnabled())
                return false;
        }

        for (int i = 0; i < lenght; i++) {
            list1 = findViewWithTagRecursively((ViewGroup) findViewById(R.id.llGroup2), "Group2" + i);
            ourViewArray[i] = list1.get(0);
            if (ourViewArray[i].isEnabled())
                return false;
        }

        return true;
    }

    private void clearChosedArray() {
        for (int i = 0; i < chosed.length; i++) {
            chosed[i] = null;
        }
    }

    private int[] generateRandomArray(int limit) {
        int[] ourArray = new int[limit];
        boolean control;
        int j = 0, backup;
        Random randomNum = new Random();

        for (int i = 0; i < ourArray.length; i++)
        {
            ourArray[i] = -1;
        }

        while (j < ourArray.length)
        {
            control = true;
            backup = randomNum.nextInt(limit);

            for (int i = 0; i < ourArray.length; i++)
            {
                if (ourArray[i] == backup)
                {
                    control = false;
                }
            }

            if (control)
            {
                ourArray[j] = backup;
                j++;
            }
        }

        return ourArray;
    }
}