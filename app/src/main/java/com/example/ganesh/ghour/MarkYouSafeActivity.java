package com.example.ganesh.ghour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MarkYouSafeActivity extends AppCompatActivity {

    private Button mSafeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_you_safe);

        initializeScreen();

        mSafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String title="I am Safe";
                String details="Dont worry!, I am safe.";
                intent.putExtra(Intent.EXTRA_SUBJECT,title);
                intent.putExtra(Intent.EXTRA_TEXT,details);
                startActivity(Intent.createChooser(intent,"Safe"));
            }
        });

    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Safe");
        setSupportActionBar(toolbar);
        mSafeBtn=(Button)findViewById(R.id.safe_button);
    }
}
