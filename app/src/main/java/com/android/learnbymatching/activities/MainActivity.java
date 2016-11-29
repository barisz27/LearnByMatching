package com.android.learnbymatching.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.database.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// بِسْــــــــــــــــــــــمِ اﷲِارَّحْمَنِ ارَّحِيم

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ListView lvMain;
    private MyAdapter adapter;
    private List<String> strings, dateStrings;
    private List<Project> projects;
    private String deleteDate = null;
    private TextView tvNoProject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = (ListView) findViewById(R.id.lvMain);

        Matchings db = new Matchings(this);
        projects = db.getAllProjects();
        db.close();

        if (projects.size() > 0)
        {
            TextView tvNoProject = (TextView) findViewById(R.id.tvNoProject);
            tvNoProject.setVisibility(View.INVISIBLE);
        }

        strings = new ArrayList<>();
        dateStrings = new ArrayList<>();

        for (int i = 0; i < projects.size(); i++)
        {
            Project pro = projects.get(i);
            String data = pro.getName();
            String date = pro.getCreate_date();

            strings.add(data);
            dateStrings.add(date);
        }

        adapter = new MyAdapter(MainActivity.this, strings, dateStrings);
        lvMain.setAdapter(adapter);
    }

    private void refresh()
    {
        Matchings db = new Matchings(this);
        projects = db.getAllProjects();
        db.close();

        if (projects.size() != 0)
        {
            tvNoProject = (TextView) findViewById(R.id.tvNoProject);
            tvNoProject.setVisibility(View.INVISIBLE);
        }

        strings = new ArrayList<>();
        dateStrings = new ArrayList<>();

        for (int i = 0; i < projects.size(); i++)
        {
            Project pro = projects.get(i);
            String data = pro.getName();
            String date = pro.getCreate_date();

            strings.add(data);
            dateStrings.add(date);
        }

        adapter = new MyAdapter(this, strings, dateStrings);
        adapter.notifyDataSetChanged();
        lvMain.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_delete:
                if (deleteDate != null)
                {
                    new android.app.AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Proje siliniyor..")
                                    .setCancelable(false)
                                    .setMessage("Silmek istediğinize emin misiniz?")
                                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Matchings db = new Matchings(MainActivity.this);
                                            db.deleteProject(deleteDate);
                                            dialogInterface.dismiss();
                                            refresh();
                                            if (projects.size() == 0){
                                                tvNoProject.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

                }
                return true;
            default:
                return false;
        }
    }


    private class MyAdapter extends BaseAdapter {

        // private String[] array, array2, array3;
        private List<String> arrayList = new ArrayList<>();
        private List<String> dateList = new ArrayList<>();
        private Context context;

        MyAdapter(Context context, List<String> arrayList, List<String> dateList) {
            this.context = context;
            this.arrayList = arrayList;
            this.dateList = dateList;
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
                convertView = inflater.inflate(R.layout.main_listview_row, null);
            }

            TextView tvMatchingTitle = (TextView) convertView.findViewById(R.id.tvMatchingTitle);
            tvMatchingTitle.setText(arrayList.get(position));

            TextView tvMatchingDate = (TextView) convertView.findViewById(R.id.tvMatchingDate);
            tvMatchingDate.setText(dateList.get(position));

            ImageButton ibPicture = (ImageButton) convertView.findViewById(R.id.ibPicture);
            ibPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            final ImageView ivPopup = (ImageView) convertView.findViewById(R.id.ivPopup);
            ivPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    deleteDate = projects.get(position).getCreate_date();
                    showPopupMenu(ivPopup);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Matchings db = new Matchings(MainActivity.this);

                    Project p = db.getProject(position + 1);

                    Intent i = new Intent(MainActivity.this, NewMatchActivity.class);
                    i.putExtra("name", p.getName());
                    i.putExtra("date", p.getCreate_date());


                    startActivity(i);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            refresh();
            return false;
        } else if (item.getItemId() == R.id.menu_newproject) {
            startActivity(new Intent(MainActivity.this, ProjectActivity.class));
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopupMenu(View v)
    {
        PopupMenu pm = new PopupMenu(this, v);
        MenuInflater inflater = pm.getMenuInflater();
        inflater.inflate(R.menu.contextual_action_menu, pm.getMenu());
        pm.setOnMenuItemClickListener(this);
        pm.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}