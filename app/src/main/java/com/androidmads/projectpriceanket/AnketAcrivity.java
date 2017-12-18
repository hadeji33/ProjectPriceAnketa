package com.androidmads.projectpriceanket;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lairg on 10.12.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AnketAcrivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    EditText edtName, edtPhone, edtFio;
    RadioGroup radioProjectType, radioRestruct, radioSiteType, edtCms;
    DataBase db;
    RequestQueue queue;
    Map<String, String> lastParams = new HashMap<>();


    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = new DataBase(this);
        db.open();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Загрузка...");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtFio = (EditText) findViewById(R.id.edtFio);
        edtCms = (RadioGroup) findViewById(R.id.edtCms);
        radioRestruct = (RadioGroup) findViewById(R.id.radioRestruct);
        radioProjectType = (RadioGroup) findViewById(R.id.radioProjectType);
        radioSiteType = (RadioGroup) findViewById(R.id.radioSiteType);

        // Initializing Queue for Volley
        queue = Volley.newRequestQueue(getApplicationContext());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().trim().length() > 0 && edtPhone.getText().toString().trim().length() > 0) {
                    if(Constants.isOnline(AnketAcrivity.this)) {
                    db.open();
                        postData();
                        db.addRec(edtName.getText().toString(), fullMap(lastParams),true);
                    }
                    else {
                        db.addRec(edtName.getText().toString(), fullMap(lastParams),false);
                    }
                    db.close();
                    startActivity(new Intent(AnketAcrivity.this, MainActivity.class));
                } else {
                    Snackbar.make(view, "Не заполнены обязательные поля", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    public Map<String, String> fullMap(Map<String, String> params){

        double restruct, siteType, cmsType, projectType, projectPrice;

        params.put(Constants.nameField, edtName.getText().toString().trim());
        params.put(Constants.phoneField, edtPhone.getText().toString().trim());
        params.put(Constants.fioField, edtFio.getText().toString().trim());

        //params.put(Constants.cmsField, edtCms.getText().toString().trim());

        int checkedRadioButtonId = radioRestruct.getCheckedRadioButtonId();
        RadioButton myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
        restruct = Double.parseDouble(myRadioButton.getContentDescription().toString());
        params.put(Constants.restructField,myRadioButton.getText().toString().trim());

        checkedRadioButtonId = radioSiteType.getCheckedRadioButtonId();
        myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
        siteType = Double.parseDouble(myRadioButton.getContentDescription().toString());
        params.put(Constants.siteTypeFiels,myRadioButton.getText().toString().trim());

        checkedRadioButtonId = edtCms.getCheckedRadioButtonId();
        myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
        cmsType = Double.parseDouble(myRadioButton.getContentDescription().toString());
        params.put(Constants.cmsField,myRadioButton.getText().toString().trim());


        checkedRadioButtonId = radioProjectType.getCheckedRadioButtonId();
        myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
        projectType = Double.parseDouble(myRadioButton.getContentDescription().toString());
        params.put(Constants.projectTypeFiels,myRadioButton.getText().toString().trim());

        projectPrice = siteType * projectType * restruct + cmsType;
        String s = "";
        params.put(Constants.projectPrice, s.valueOf(projectPrice));

        return params;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(this, LoginActivity.class));
        }
        //updateUI(currentUser);
    }

    public void postData() {

        progressDialog.show();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Toast.makeText(AnketAcrivity.this, "Анкета успешно заполнена", Snackbar.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AnketAcrivity.this, "Попробуйте еще раз", Snackbar.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AnketAcrivity.this, "Ошибка при отправке данных анкеты", Snackbar.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return fullMap(params);

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
