package com.android.learnbymatching.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.name;

/**
 * Created by Lenovo on 22.11.2016.
 *
 */

public class Matchings {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "matching_name";
    public static final String KEY_DATE = "matching_date";
    public static final String KEY_POSITION_G1 = "position_g1";
    public static final String KEY_VALUE_G1 = "value_g1";
    public static final String KEY_POSITION_G2 = "position_g2";
    public static final String KEY_VALUE_G2 = "value_g2";

    private static final String DATABASE_NAME = "Matchingsdb";
    private static final String DATABASE_TABLE = "matchingTable";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDataBase;

    public String getData() {

        String[] colums = new String[]{KEY_ROWID, KEY_NAME, KEY_DATE, KEY_POSITION_G1, KEY_VALUE_G1, KEY_POSITION_G2, KEY_VALUE_G2};
        Cursor c = ourDataBase.query(DATABASE_TABLE, colums, null, null, null, null, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID); // iRow = 0;
        int iName = c.getColumnIndex(KEY_NAME); // iName = 1;
        int iHotness = c.getColumnIndex(KEY_DATE); // iHotness = 2;
        int iPos1 = c.getColumnIndex(KEY_POSITION_G1); // iHotness = 2;
        int iValue1 = c.getColumnIndex(KEY_VALUE_G1); // iHotness = 2;
        int iPos2 = c.getColumnIndex(KEY_POSITION_G2); // iHotness = 2;
        int iValue2 = c.getColumnIndex(KEY_VALUE_G2); // iHotness = 2;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result += c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iHotness) +
            c.getString(iPos1) + " " + c.getString(iValue1) + " " + c.getString(iPos2) + " " + c.getString(iValue2) + "\n";
        }

        return result;
    }

    public String getName(long l) throws SQLException {
        String[] colums = new String[]{KEY_ROWID, KEY_NAME, KEY_DATE};
        Cursor c = ourDataBase.query(DATABASE_TABLE, colums, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst(); // cursorun ilk elemnına gittik
            String name = c.getString(1);

            return name;
        }
        return null;
    }

    public String getHotness(long l) throws SQLException{
        String[] colums = new String[]{KEY_ROWID, KEY_NAME, KEY_DATE};
        Cursor c = ourDataBase.query(DATABASE_TABLE, colums, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String hotness = c.getString(2);

            return hotness;
        }
        return null;
    }

    public void updateEntry(long lRow, String mName, String mHotness) throws SQLException{
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, mName);
        cv.put(KEY_DATE, mHotness);
        ourDataBase.update(DATABASE_TABLE, cv, KEY_ROWID + "=" + lRow, null);
    }

    public void deleteEntry(long lRow1) throws SQLException{
        ourDataBase.delete(DATABASE_TABLE, KEY_ROWID + "=" + lRow1, null);
    }

    public void deleteAllEntries() throws SQLException{
        String[] colums = new String[]{KEY_ROWID, KEY_NAME, KEY_DATE};
        Cursor c = ourDataBase.query(DATABASE_TABLE, colums, null, null, null, null, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            ourDataBase.delete(DATABASE_TABLE, KEY_ROWID + "=" + c.getPosition(), null);
        }
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String SQL_CODE = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " TEXT NOT NULL, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_POSITION_G1 + " INTEGER, " +
                    KEY_POSITION_G2 + " INTEGER, " +
                    KEY_VALUE_G1 + " TEXT NOT NULL, " +
                    KEY_VALUE_G2 + " TEXT NOT NULL, " +
                    KEY_DATE + " TEXT NOT NULL);";

            db.execSQL(SQL_CODE);
            Log.i("TAG", "Database oluşturuldu");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
            Log.i("TAG", i + " den " + i1 + "e guncelleniyor");
        }
    }

    public Matchings(Context c) {
        ourContext = c;
    }

    public Matchings open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDataBase = ourHelper.getWritableDatabase();
        return this;
    }

    public long createEntry(String id, String name, String date, int position_g1, String value_g1, int position_g2, String value_g2) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ROWID, id);
        cv.put(KEY_NAME, name);
        cv.put(KEY_DATE, date);
        cv.put(KEY_POSITION_G1, position_g1);
        cv.put(KEY_VALUE_G1, value_g1);
        cv.put(KEY_POSITION_G2, position_g2);
        cv.put(KEY_VALUE_G2, value_g2);

        return ourDataBase.insert(DATABASE_TABLE, null, cv);
    }

    public void close() {
        ourHelper.close();
    }
}