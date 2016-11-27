package com.android.learnbymatching.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lenovo on 22.11.2016.
 *
 */

public class Matchings extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteOpenHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "matchings_db";

    private static final String TABLE_PROJECTS = "projects";
    private static final String TABLE_MATCHS = "matchs";

    private static final String KEY_ID = "_id";
    private static final String KEY_CREATE_DATE = "create_date";

    private static final String KEY_NAME = "name";

    private static final String KEY_MATCH = "match";

    private static final String CREATE_TABLE_PROJECTS = "CREATE TABLE "
            + TABLE_PROJECTS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME
            + " TEXT, " + KEY_CREATE_DATE + " TEXT);";

    private static final String CREATE_TABLE_MATCHS = "CREATE TABLE "
            + TABLE_MATCHS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_MATCH
            + " TEXT, " + KEY_CREATE_DATE + " TEXT);";

    public Matchings(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_PROJECTS);
        db.execSQL(CREATE_TABLE_MATCHS);
        Log.i(TAG, "TABLOLAR OLUŞTURULDU");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHS);

        onCreate(db);
        Log.i(TAG, oldVersion + " dan " + newVersion + " a güncelleniyor..");
    }

    public long createProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, project.getName());
        values.put(KEY_CREATE_DATE, project.getCreate_date());

        long project_id = db.insert(TABLE_PROJECTS, null, values);

        Log.i(TAG, project.getName() + " " + project_id + " numaralı yere yerleşti");

        return project_id;
    }

    public Project getProject(long project_id)
    {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PROJECTS + " WHERE " + KEY_ID + " = " + project_id;

        Log.i(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            c.moveToFirst();
        }

        Project project = new Project();

        project.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        project.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        project.setCreate_date(c.getString(c.getColumnIndex(KEY_CREATE_DATE)));

        return project;
    }

    public long createMatchs(Matchs matchs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATCH, matchs.getFirst() + "-" + matchs.getSecond());
        values.put(KEY_CREATE_DATE, matchs.getCreate_date());

        long matchs_id = db.insert(TABLE_MATCHS, null, values);

        Log.i(TAG, matchs.getFirst() + " " + matchs.getSecond() + " " + matchs_id + " numaralı yere yerleşti");

        return matchs_id;
    }

    public Matchs getMatchs(long match_id) {

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MATCHS + " WHERE " + KEY_ID + " = " + match_id;

        Log.i(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            c.moveToFirst();
        }

        Matchs matchs = new Matchs();

        matchs.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        matchs.setFirst(c.getString(c.getColumnIndex(KEY_MATCH)));
        matchs.setSecond(c.getString(c.getColumnIndex(KEY_MATCH)));
        matchs.setCreate_date(c.getString(c.getColumnIndex(KEY_CREATE_DATE)));

        return matchs;
    }
}