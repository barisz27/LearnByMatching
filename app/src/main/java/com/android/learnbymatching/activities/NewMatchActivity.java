package com.android.learnbymatching.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.database.Matchs;
import com.android.learnbymatching.database.Project;
import com.android.learnbymatching.dialog.NewMatchDialogFragment;
import com.android.learnbymatching.dialog.NewMatchUpdateListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NewMatchActivity extends AppCompatActivity implements NewMatchUpdateListener {

    private ArrayList<String> firstArray, secondArray;
    private MyAdapter adapter;
    private Project project;
    private ListView l1, l2;
    private List<Matchs> myMatchsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        onFinishInflate();

        project = new Project();
        project.setName(getIntent().getExtras().getString("name"));
        project.setCreate_date(getIntent().getExtras().getString("date"));

        // Log.d("baris", project.getName() + " " + date);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(project.getName());
        actionBar.setSubtitle(project.getCreate_date());
        addListFromDb(project.getCreate_date());
    }

    public void newButtonOnClick(View view) {
        NewMatchDialogFragment dialog = NewMatchDialogFragment.newInstance();
        dialog.show(getFragmentManager(), "NewMatchDialogFragment");
    }

    // my interface
    @Override
    public void onUpdate(int position, String first, String second) {
        updateToDb(position, first, second);
    }

    private void updateToDb(int position, String first, String second)
    {
        Matchs matchs = new Matchs();
        matchs.setFirst(first);
        matchs.setSecond(second);
        matchs.setCreate_date(project.getCreate_date());

        Matchings db = new Matchings(NewMatchActivity.this);
        db.updateMatchs(matchs, myMatchsArray.get(position).getFirst());

        firstArray = new ArrayList<>();
        secondArray = new ArrayList<>();

        addListFromDb(matchs.getCreate_date());
    }

    // my other interface
    @Override
    public void onCreateNew(String first, String second) {
        saveToDb(first, second);
        addListFromDb(project.getCreate_date());
    }

    private void onFinishInflate() {
        l1 = (ListView) findViewById(R.id.l1);
        l2 = (ListView) findViewById(R.id.l2);

        firstArray = new ArrayList<>();
        secondArray = new ArrayList<>();
    }

    public void startGameActivity(View v)
    {
        Intent i = new Intent(this, GameActivity.class);

        Bundle mBundle = new Bundle();
        mBundle.putStringArrayList("first", firstArray);
        mBundle.putStringArrayList("second", secondArray);

        i.putExtras(mBundle);
        startActivity(i);
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

            final TextView lvrTvName = (TextView) convertView.findViewById(R.id.lvrTvName);
            lvrTvName.setText(array.get(position));

            final View card = convertView.findViewById(R.id.card);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewMatchDialogFragment dialogFragment =
                            NewMatchDialogFragment.newInstance(position, firstArray.get(position), secondArray.get(position));
                    dialogFragment.show(getFragmentManager(), "NewMatchDialogFragment");
                }
            });

            ImageButton bDelete = (ImageButton) convertView.findViewById(R.id.bDelete);
            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFromDb(myMatchsArray.get(position).getFirst());
                    Log.d("NewMatchActivity", position + "");

                }
            });

            return convertView;
        }
    }

    private void deleteFromDb(String name) {
        Matchings db = new Matchings(NewMatchActivity.this);
        db.deleteMatchs(name);
        myMatchsArray = new ArrayList<>();
        myMatchsArray = db.getMatchByDate(project.getCreate_date());
        firstArray.clear();
        secondArray.clear();

        for (int i = 0; i < myMatchsArray.size(); i++)
        {
            int whereShortLine = myMatchsArray.get(i).getFirst().indexOf("-");
            String first = myMatchsArray.get(i).getFirst().substring(0, whereShortLine);
            String second = myMatchsArray.get(i).getFirst().substring(whereShortLine + 1, myMatchsArray.get(i).getFirst().length());

            firstArray.add(i, first);
            secondArray.add(i, second);
        }
        adapter = new MyAdapter(this, firstArray);
        l1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter = new MyAdapter(this, secondArray);
        l2.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private long saveToDb(String first, String second) {
        Matchs myMatchs = new Matchs();
        myMatchs.setFirst(first);
        myMatchs.setSecond(second);
        myMatchs.setCreate_date(project.getCreate_date());

        Matchings db = new Matchings(NewMatchActivity.this);

        long r = db.createMatchs(myMatchs);
        db.close();

        Toast.makeText(NewMatchActivity.this, "Eklendi", Toast.LENGTH_SHORT).show();

        return r;
    }

    private void addListFromDb(String createDate)
    {
        Matchings db = new Matchings(NewMatchActivity.this);

        myMatchsArray = db.getMatchByDate(project.getCreate_date());
        firstArray.clear();
        secondArray.clear();

        for (int i = 0; i < myMatchsArray.size(); i++)
        {
            int whereShortLine = myMatchsArray.get(i).getFirst().indexOf("-");
            String first = myMatchsArray.get(i).getFirst().substring(0, whereShortLine);
            String second = myMatchsArray.get(i).getFirst().substring(whereShortLine + 1, myMatchsArray.get(i).getFirst().length());

            firstArray.add(i, first);
            secondArray.add(i, second);
        }

        l1.setAdapter(new MyAdapter(this, firstArray));
        l2.setAdapter(new MyAdapter(this, secondArray));
    }

}
