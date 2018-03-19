package com.example.ganesh.ghour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class BloodWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_web_view);

        initializeScreen();

        WebView mywebview = (WebView) findViewById(R.id.blood_web_view);
        mywebview.loadUrl("http://192.168.43.140/index.php");


    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("ZeroHour");
        setSupportActionBar(toolbar);
    }
}
