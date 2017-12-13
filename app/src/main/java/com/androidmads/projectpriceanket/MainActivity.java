package com.androidmads.projectpriceanket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    FloatingActionButton fab;
    EditText edtName, edtPhone;
    RequestQueue queue;
    TextView usrName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        usrName = (TextView) findViewById(R.id.usrName);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(this, LoginActivity.class));
        } else  {
           usrName.setText(currentUser.getEmail());
        }

    }

    public void startAnket(View view){
        startActivity(new Intent(this, AnketAcrivity.class));
    }
    public void showProjects(View view){
        startActivity(new Intent(this, ProjectAcrivity.class));
    }

    public void logOut(View view){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

}
