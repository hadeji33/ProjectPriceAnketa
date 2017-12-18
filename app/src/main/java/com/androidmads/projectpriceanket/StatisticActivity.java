package com.androidmads.projectpriceanket;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class StatisticActivity extends AppCompatActivity {

    WebView webView;
    String htmldata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        webView = (WebView) findViewById (R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(Constants.statistics_url);
        //new NewThread().execute();
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg) {
            Document doc;
            /*WebDriver driver = new FirefoxDriver();
            driver.get(Constants.statistics_url);
            doc = Jsoup.parse(driver.getPageSource());*/
            //doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", "style.css");
            //htmldata = doc.outerHtml();


            return null;
        }
        @Override
        protected void onPostExecute(String result){
            webView.loadDataWithBaseURL("file:///android_asset/.", htmldata, "text/html", "UTF-8", null);
        }
    }
}
