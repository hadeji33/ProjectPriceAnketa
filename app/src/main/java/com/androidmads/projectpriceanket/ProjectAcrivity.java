package com.androidmads.projectpriceanket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;

public class ProjectAcrivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ListView lvData;
    DataBase db = new DataBase(this);
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    Switch sv;
    String selection = "posted = 1";
    private static final int CM_DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        sv = (Switch) findViewById(R.id.switch1);

        if (savedInstanceState != null) {
            Bundle extras = getIntent().getExtras();
            selection= extras.getString("STRING_I_NEED");
            sv.setChecked(true);
        }
        db = new DataBase(this);
        db.open();


        cursor = db.getAllData(selection);
        startManagingCursor(cursor);

        String[] from = new String[] { db.COLUMN_NAME,db.COLUMN_DATE  };
        int[] to = new int[] { R.id.name, R.id.date };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        scAdapter.notifyDataSetChanged();
        if (sv != null) {
            sv.setOnCheckedChangeListener(this);
        }
    }


    public void backToMain(View view){startActivity(new Intent(ProjectAcrivity.this, MainActivity.class));}

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить запись");
        if (sv.isChecked()){
            menu.add(1, 2, 0, "Синхронизировать");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        db.delRec(acmi.id);
        cursor.requery();

        return true;
    }
    return super.onContextItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        db.open();
        String selection = "posted = 1";
        if (isChecked){
            selection = "posted = 0";
        }


        cursor = db.getAllData(selection);
        startManagingCursor(cursor);

        String[] from = new String[] { "name","date"  };
        int[] to = new int[] { R.id.name, R.id.date };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        scAdapter.notifyDataSetChanged();



    }
}
