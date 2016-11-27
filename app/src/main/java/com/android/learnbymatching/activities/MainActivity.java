package com.android.learnbymatching.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.learnbymatching.ProjectActivity;
import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.database.Matchs;
import com.android.learnbymatching.database.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.data;

// بِسْــــــــــــــــــــــمِ اﷲِارَّحْمَنِ ارَّحِيم

public class MainActivity extends AppCompatActivity {

    private ListView lvMain;
    private MyAdapter adapter;
    private List<String> strings;
    List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = (ListView) findViewById(R.id.lvMain);

        findViewById(R.id.bNew).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                startActivity(new Intent(MainActivity.this, ProjectActivity.class));
                return false;
            }
        });


        String[] tArray = {"title 1", "title 2"};

        Matchings db = new Matchings(this);
        projects = db.getAllProjects();
        db.close();

         strings = new ArrayList<>();

        for (int i = 0; i < projects.size(); i++)
        {
            Project pro = projects.get(i);
            String data = pro.getName();

            strings.add(data);
        }

        adapter = new MyAdapter(MainActivity.this, strings);
        lvMain.setAdapter(adapter);
    }

    public void refresh(View v)
    {
        Matchings db = new Matchings(this);
        projects = db.getAllProjects();
        db.close();

        strings = new ArrayList<>();

        for (int i = 0; i < projects.size(); i++)
        {
            Project pro = projects.get(i);
            String data = pro.getName();

            strings.add(data);
        }

        adapter = new MyAdapter(this, strings);
        adapter.notifyDataSetChanged();
        lvMain.setAdapter(adapter);
    }

    public void buttonOnClick(View view) {
        Intent i = new Intent(MainActivity.this, NewMatchActivity.class);
        startActivity(i);
    }

    public void bOnClick(View view)
    {
        Project myProject = new Project();
        myProject.setId(1);
        myProject.setName("Barışın projesi");
        myProject.setCreate_date(getFullTime());

        // eşleşme
        Matchs myMatchs = new Matchs();
        myMatchs.setFirst("şinasi");
        myMatchs.setSecond("şair evlenmesi");
        myMatchs.setCreate_date(getFullTime());
        myMatchs.setId(2);

        Matchings db = new Matchings(this);
        db.createProject(myProject);
        db.createMatchs(myMatchs);
        db.close();

        Project p = db.getProject(1);

        String s = p.getId() + " " + p.getName() + " " + p.getCreate_date();

        TextView tv = new TextView(this);
        tv.setText(s);

        Dialog d = new Dialog(this);
        d.setContentView(tv);
        d.show();
    }

    private class MyAdapter extends BaseAdapter {

        // private String[] array, array2, array3;
        private List<String> arrayList = new ArrayList<>();
        private Context context;

        MyAdapter(Context context, List<String> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
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
            tvMatchingTitle.setText(arrayList.get(position));

            TextView tvMatchingDate = (TextView) convertView.findViewById(R.id.tvMatchingDate);
           // tvMatchingDate.setText(array2[position]);

            TextView tvMatchingRandom = (TextView) convertView.findViewById(R.id.tvMatchingRandom);
            //tvMatchingRandom.setText(array3[position]);

            ImageButton ibPicture = (ImageButton) convertView.findViewById(R.id.ibPicture);
            ibPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            return convertView;
        }
    }

    public static String getFullTime(){ // tarih + saat gösterir..
        String fullTime;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fullTime = dateFormat.format(c.getTime());

        return fullTime;
    }

}