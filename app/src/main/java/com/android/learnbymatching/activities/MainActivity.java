package com.android.learnbymatching.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.dialog.NewMatchDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.android.learnbymatching.R.id.bDelete;
import static com.android.learnbymatching.R.id.lvrTvName;

// بِسْــــــــــــــــــــــمِ اﷲِارَّحْمَنِ ارَّحِيم

public class MainActivity extends AppCompatActivity {

    private ListView lvMain;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = (ListView) findViewById(R.id.lvMain);
        tv = (TextView) findViewById(R.id.textView);

        Matchings entry = new Matchings(MainActivity.this);
        entry.open();
        entry.createEntry("time", "isim", "tarih", 1, "deger", 2, "deger");
        String s = entry.getData();
        entry.close();

        tv.setText(s);

        String[] tArray = {"title 1", "title 2"};
        String[] dateArray = {"date 1", "date 2"};
        String[] raArray = {"random 1", "random 2"};

        lvMain.setAdapter(new MyAdapter(MainActivity.this, tArray, dateArray, raArray));
    }

    public void buttonOnClick(View view) {
        Intent i = new Intent(MainActivity.this, NewMatchActivity.class);
        startActivity(i);
    }

    private class MyAdapter extends BaseAdapter {

        private String[] array, array2, array3;
        private Context context;

        MyAdapter(Context context, String[] array, String[] array2, String[] array3) {
            this.context = context;
            this.array = array;
            this.array2 = array2;
            this.array3 = array3;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = (View) inflater.inflate(R.layout.main_listview_row, null);
            }

            TextView tvMatchingTitle = (TextView) convertView.findViewById(R.id.tvMatchingTitle);
            tvMatchingTitle.setText(array[position]);

            TextView tvMatchingDate = (TextView) convertView.findViewById(R.id.tvMatchingDate);
            tvMatchingDate.setText(array2[position]);

            TextView tvMatchingRandom = (TextView) convertView.findViewById(R.id.tvMatchingRandom);
            tvMatchingRandom.setText(array3[position]);

            ImageButton ibPicture = (ImageButton) convertView.findViewById(R.id.ibPicture);
            ibPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            return convertView;
        }
    }
}