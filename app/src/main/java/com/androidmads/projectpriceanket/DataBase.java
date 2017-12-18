package com.androidmads.projectpriceanket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

/**
 * Created by lairg on 11.12.2017.
 */

public class DataBase {

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "ankets";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_POSTED = "posted";
    public static final String COLUMN_TXT = "text";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " + COLUMN_DATE + " text, " + COLUMN_POSTED + " integer," +
                    COLUMN_TXT + " text" +
                    ");";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DataBase(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    public Cursor getAllData(String selection) {
        return mDB.query(DB_TABLE, null, selection, null, null, null, null);
    }

    public void addRec(String name, Map<String, String> map, boolean online) {

        String orgmap = map.toString();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);
        cv.put(COLUMN_DATE, date.toString());
        int p = 0;
        if (online) {
            p = 1;
        }
        cv.put(COLUMN_POSTED,p);
        cv.put(COLUMN_TXT, orgmap);
        mDB.insert(DB_TABLE, null, cv);
    }

    public modelClass getDbLine(long id){
        String select = COLUMN_ID + " = " + id;
        Cursor c = getAllData(select);
        modelClass model = new modelClass();
        if (c.moveToFirst()) {
            model.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            model.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
            model.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            model.setPosted(c.getInt(c.getColumnIndex(COLUMN_POSTED)));
            model.setText(c.getString(c.getColumnIndex(COLUMN_TXT)));
            return model;
        }
        return  null;
    }

    public void updateLine (modelClass model) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID,model.getId());
        cv.put(COLUMN_DATE,model.getDate());
        cv.put(COLUMN_NAME,model.getName());
        cv.put(COLUMN_POSTED,model.getPosted());
        cv.put(COLUMN_TXT,model.getText());
        mDB.update(DB_TABLE, cv,COLUMN_ID + " = " + model.getId(), null);

    }

    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }


    private class DBHelper   extends SQLiteOpenHelper {


        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}
}
