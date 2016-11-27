package com.android.learnbymatching;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.learnbymatching.activities.MainActivity;
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

                Project project = new Project();
                project.setId(0);
                project.setName(input);
                project.setCreate_date(MainActivity.getFullTime());

                Matchings db = new Matchings(ProjectActivity.this);
                db.createProject(project);
                db.close();

                Toast.makeText(ProjectActivity.this, "Yeni proje oluşturuldu", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
