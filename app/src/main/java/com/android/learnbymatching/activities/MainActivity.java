package com.android.learnbymatching.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.database.Project;
import com.android.learnbymatching.dialog.RenameProjectDialogFragment;
import com.android.learnbymatching.prefs.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// بِسْــــــــــــــــــــــمِ اﷲِارَّحْمَنِ ارَّحِيم

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, RenameProjectDialogFragment.RenameUpdateListener {

    private ListView lvMain;
    private MyAdapter adapter;
    private List<String> projectNames, projectDates;
    private List<Project> projects;
    private String deleteDate = null;
    private String deleteName = null;
    private TextView tvNoProject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = (ListView) findViewById(R.id.lvMain);
        tvNoProject = (TextView) findViewById(R.id.tvNoProject);

        Matchings db = new Matchings(this);
        projects = db.getAllProjects();
        db.close();

        int projectCount = projects.size();

        if (projectCount > 0) {
            tvNoProject.setVisibility(View.INVISIBLE);
        }

        projectNames = new ArrayList<>();
        projectDates = new ArrayList<>();

        for (Project pro : projects) {
            projectNames.add(pro.getName());
            projectDates.add(pro.getCreate_date());
        }

        adapter = new MyAdapter(MainActivity.this, projectNames, projectDates);
        lvMain.setAdapter(adapter);
    }

    private void refresh()
    {
        Matchings db = new Matchings(this);
        projects = db.getAllProjects();
        db.close();

        int projectCount = projects.size();

        if (projectCount != 0) {
            tvNoProject.setVisibility(View.INVISIBLE);
        }

        projectNames.clear();
        projectDates.clear();

        for (Project pro : projects) {
            projectNames.add(pro.getName());
            projectDates.add(pro.getCreate_date());
        }

        adapter = new MyAdapter(this, projectNames, projectDates);
        lvMain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            case R.id.menu_rename:
                if (deleteDate != null)
                {
                    showRenameDialog(deleteDate);
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRename(String date, String newName)
    {
        Matchings db = new Matchings(MainActivity.this);
        db.updateProject(date, newName);
        db.close();

        refresh();
    }

    private class MyAdapter extends BaseAdapter {

        private List<String> nameList = new ArrayList<>();
        private List<String> dateList = new ArrayList<>();
        private Context context;

        MyAdapter(Context context, List<String> nameList, List<String> dateList) {
            this.context = context;
            this.nameList = nameList;
            this.dateList = dateList;
        }

        @Override
        public int getCount() {
            return nameList.size();
        }

        @Override
        public Object getItem(int position) {
            return nameList.get(position);
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
            tvMatchingTitle.setText(nameList.get(position));

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
                    deleteName = projects.get(position).getName();
                    showPopupMenu(ivPopup,  projects.get(position).getCreate_date());
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

    public static String getFullTime(){
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
        } else if (item.getItemId() == R.id.menu_shake) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.shake);
            lvMain.startAnimation(anim);
            return false;
        } else if (item.getItemId() == R.id.menu_prefs) {
            startActivity(new Intent(MainActivity.this, Prefs.class));
            return false;
        } else if (item.getItemId() == R.id.menu_move) {
            /*Animation animation = AnimationUtils.loadAnimation(this, R.anim.move);
            lvMain.startAnimation(animation);*/
            startActivity(new Intent("android.intent.action.SHOW_BRIGHTNESS_DIALOG"));
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopupMenu(View v, String date)
    {
        deleteDate = date;
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

    private void showRenameDialog(String project_date){
        RenameProjectDialogFragment dialogFragment = RenameProjectDialogFragment.newInstance(project_date, deleteName);
        dialogFragment.show(getFragmentManager(), "RenameProjectDialogFragment");
    }

    @Override
    public void onBackPressed() {
        boolean isAsk;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAsk = preferences.getBoolean("PREF_ASK_WHILE_CLOSING", true);
        if (isAsk) {
            new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Uygulamadan çıkılıyor..")
                            .setCancelable(false)
                    .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                            .setMessage("Çıkmak istediğinize emin misiniz?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity.this.finish();
                                }
                            }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        } else {
            MainActivity.this.finish();
        }
    }
}