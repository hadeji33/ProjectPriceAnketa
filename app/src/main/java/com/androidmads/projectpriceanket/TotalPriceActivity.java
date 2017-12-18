package com.androidmads.projectpriceanket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class TotalPriceActivity extends AppCompatActivity {

    TextView totalPrice, design, cmsPrice, programming, mobileApp, filling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);

        String textMap = getIntent().getStringExtra("key");
        Map<String,String> data = new HashMap<String,String>();
        String str = textMap.replace("{", "").replace("}", "");
        String[] arr = str.split(", ");
        for (String mapline : arr) {
            String[] splited = mapline.split("=");
            data.put(splited[0], splited[1]);
        }
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        design = (TextView) findViewById(R.id.design);
        cmsPrice = (TextView) findViewById(R.id.cmsPrice);
        programming = (TextView) findViewById(R.id.programming);
        mobileApp = (TextView) findViewById(R.id.mobileApp);
        filling = (TextView) findViewById(R.id.filling);

        totalPrice.setText(totalPrice.getText().toString() + ": " +  data.get("totalPrice"));
        design.setText(design.getText().toString() + ": " +  data.get("design"));
        cmsPrice.setText(cmsPrice.getText().toString() + ": " +  data.get("cmsPrice"));
        programming.setText(programming.getText().toString() + ": " +  data.get("programming"));
        mobileApp.setText(mobileApp.getText().toString() + ": " +  data.get("mobileApp"));
        filling.setText(filling.getText().toString() + ": " +  data.get("filling"));
    }
}
