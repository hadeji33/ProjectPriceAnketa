package com.androidmads.projectpriceanket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ProjectAcrivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ListView lvData;
    DataBase db = new DataBase(this);
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    Switch sv;

    String selection = "posted = 1";
    private static final int CM_DELETE_ID = 1;
    ProgressDialog progressDialog;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Загрузка...");
        sv = (Switch) findViewById(R.id.switch1);

        if (savedInstanceState != null) {
            Bundle extras = getIntent().getExtras();
            selection= extras.getString("STRING_I_NEED");
            sv.setChecked(true);
        }
        db = new DataBase(this);
        db.open();
        queue = Volley.newRequestQueue(getApplicationContext());


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

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                modelClass thisLine = db.getDbLine(position);
                modelClass thisLine1 = db.getDbLine(id);

                Intent intent = new Intent(ProjectAcrivity.this, TotalPriceActivity.class);
                intent.putExtra("key", thisLine1.convertTextToMap(thisLine1.getResult()).toString());
                startActivity(intent);
            }
        });

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
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == CM_DELETE_ID) {
        db.delRec(acmi.id);
        cursor.requery();
        return true;
        } else if (item.getItemId() == 2){
            modelClass thisLine = db.getDbLine(acmi.id);
            Map<String,String> modelParams = thisLine.convertTextToMap(thisLine.getText());
            try{
                postData(modelParams,thisLine);
            } catch (Exception e){
                Log.d("Exception", "Не удалось синхронизировать");
            }

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


    public void postData(final Map<String,String> params, final modelClass thisLine) {

        progressDialog.show();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Toast.makeText(ProjectAcrivity.this, "Успешно синхронизировано", Snackbar.LENGTH_LONG).show();
                            thisLine.setPosted(1);
                            db.updateLine(thisLine);
                            cursor.requery();
                        } else {
                            Toast.makeText(ProjectAcrivity.this, "Try Again", Snackbar.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProjectAcrivity.this, "Не удалось синхронизировать", Snackbar.LENGTH_LONG).show();
                Exception e = new Exception();
                try {
                    throw e;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
