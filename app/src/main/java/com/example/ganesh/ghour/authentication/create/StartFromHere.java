package com.example.ganesh.ghour.authentication.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ganesh.ghour.R;
import com.example.ganesh.ghour.authentication.create.iamauthority.AuthorityEmailFragment;
import com.example.ganesh.ghour.authentication.create.imuser.UserEmail;
import com.example.ganesh.ghour.authentication.join.JoinActivity;

public class StartFromHere extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    private final String LOG_TAG = StartFromHere.class.getName();

    private Button mUserBtn, mAuthorityBtn;
    private TextView mJoinGhour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_from_here);

        initializeScreen();


        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);

        mUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEmail userEmailFragment = new UserEmail();
                FragmentManager fm = getSupportFragmentManager();

                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.start_from_here_relaytive_layout, userEmailFragment, "UserEmail");
                ft.addToBackStack("Add");
                ft.commit();
                finish();
            }
        });

        mAuthorityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorityEmailFragment userEmailFragment = new AuthorityEmailFragment();
                FragmentManager fm = getSupportFragmentManager();

                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.start_from_here_relaytive_layout, userEmailFragment, "AuthorityEmail");
                ft.addToBackStack("Add");
                ft.commit();
                finish();
            }
        });

        mJoinGhour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartFromHere.this, JoinActivity.class));
                finish();
            }
        });

    }


    private void initializeScreen() {
        mUserBtn = (Button) findViewById(R.id.start_from_here_user_btn);
        mAuthorityBtn = (Button) findViewById(R.id.start_from_here_authority_btn);
        mJoinGhour=(TextView)findViewById(R.id.join_ghour);
    }


    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack("UserEmail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.popBackStack("AuthorityEmail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
