package com.android.learnbymatching.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.database.Matchs;
import com.android.learnbymatching.dialog.NewMatchDialogFragment;
import com.android.learnbymatching.dialog.NewMatchUpdateListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewMatchActivity extends AppCompatActivity implements NewMatchUpdateListener, View.OnLongClickListener {

    private ArrayList<String> firstArray, secondArray;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        onFinishInflate();

    }

    public void newButtonOnClick(View view) {
        NewMatchDialogFragment dialog = NewMatchDialogFragment.newInstance();
        dialog.show(getFragmentManager(), "NewMatchDialogFragment");
    }

    // my interface
    @Override
    public void onUpdate(int position, String first, String second) {
        updateListWithNewValue(position, first, firstArray);
        updateListWithNewValue(position, second, secondArray);
        // ilk listede ikinci listeye yeni değer
        // girildiğinde listenin update olma sorunu çözen kod
        adapter = new MyAdapter(this, secondArray);
        adapter.notifyDataSetChanged();
    }

    // my other interface
    @Override
    public void onCreateNew(String first, String second) {
        addToListAndUpdate(first, firstArray);
        addToListAndUpdate(second, secondArray);
        saveToDb(first, second, getFullTime(), firstArray.size());
    }

    private void onFinishInflate() {
        Button button = (Button) findViewById(R.id.button2);
        button.setOnLongClickListener(this);
        ListView l1 = (ListView) findViewById(R.id.l1);
        ListView l2 = (ListView) findViewById(R.id.l2);

        firstArray = new ArrayList<>();
        secondArray = new ArrayList<>();

        l1.setAdapter(new MyAdapter(this, firstArray));
        l2.setAdapter(new MyAdapter(this, secondArray));
    }

    @Override
    public boolean onLongClick(View v) {
        Intent i = new Intent(this, GameActivity.class);

        Bundle mBundle = new Bundle();
        mBundle.putStringArrayList("first", firstArray);
        mBundle.putStringArrayList("second", secondArray);

        i.putExtras(mBundle);
        startActivity(i);
        return true;
    }

    private class MyAdapter extends BaseAdapter {

        private ArrayList<String> array;
        private Context context;

        MyAdapter(Context context, ArrayList<String> array) {
            this.context = context;
            this.array = array;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = (View) inflater.inflate(R.layout.listview_row, null);
            }

            TextView lvrTvName = (TextView) convertView.findViewById(R.id.lvrTvName);
            lvrTvName.setText(array.get(position));

            ImageButton bDelete = (ImageButton) convertView.findViewById(R.id.bDelete);
            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    array.remove(position);
                    notifyDataSetChanged();
                }
            });

            View card = convertView.findViewById(R.id.card);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewMatchDialogFragment dialogFragment =
                            NewMatchDialogFragment.newInstance(position, firstArray.get(position), secondArray.get(position));
                    dialogFragment.show(getFragmentManager(), "NewMatchDialogFragment");
                }
            });

            return convertView;
        }
    }

    private void addToListAndUpdate(String item, ArrayList<String> list) {
        list.add(item);
        adapter = new MyAdapter(this, list);
        adapter.notifyDataSetChanged();
    }

    private void updateListWithNewValue(int position, String first, ArrayList<String> list) {
        list.remove(position);
        list.add(position, first);
        adapter = new MyAdapter(this, list);
        adapter.notifyDataSetChanged();
    }

    private String getFullTime(){ // tarih + saat gösterir..
        String fullTime;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fullTime = dateFormat.format(c.getTime());

        return fullTime;
    }

    private long saveToDb(String first, String second, String date, int position) {
        Matchs myMatchs = new Matchs();
        myMatchs.setId(position);
        myMatchs.setFirst(first);
        myMatchs.setSecond(second);
        myMatchs.setCreate_date(date);

        Matchings db = new Matchings(NewMatchActivity.this);

        long r = db.createMatchs(myMatchs);
        db.close();

        return r;
    }
}
