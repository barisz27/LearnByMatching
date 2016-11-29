package com.android.learnbymatching.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.learnbymatching.R;
import com.android.learnbymatching.database.Matchings;
import com.android.learnbymatching.database.Project;

public class ProjectActivity extends AppCompatActivity {

    private EditText etProjectName;
    private Button bNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        etProjectName = (EditText) findViewById(R.id.etProjectName);
        bNext = (Button) findViewById(R.id.bNext);

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = etProjectName.getText().toString();

                String create_date = MainActivity.getFullTime();

                Project project = new Project();
                project.setId(0);
                project.setName(input);
                project.setCreate_date(create_date);

                Matchings db = new Matchings(ProjectActivity.this);
                db.createProject(project);
                db.close();

                Toast.makeText(ProjectActivity.this, "Yeni proje oluşturuldu", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
