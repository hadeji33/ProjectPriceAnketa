package com.androidmads.projectpriceanket;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class StatisticActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        webView = (WebView) findViewById (R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(Constants.statistics_url, null, "text/html", "UTF-8", null);
        //webView.loadUrl(Constants.statistics_url);
    }
}
