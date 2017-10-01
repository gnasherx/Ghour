package com.example.ganesh.ghour.authentication.join;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ganesh.ghour.MainActivity;
import com.example.ganesh.ghour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinActivity extends AppCompatActivity {

    private static String TAG = JoinActivity.class.getName();

    private EditText mUserEmailEditText, mUserPasswordEditText;
    private Button mJoinGhour;
    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String currentuser;
    private String selectWhoAreYou;
    private Spinner mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        initializeScreen();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("ghourUsers");
        mAuth = FirebaseAuth.getInstance();
        mDatabase.keepSynced(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentuser = user.getUid();
                } else {
                    Log.d(TAG, "onAuthStateChanged: Error while setting current user!");
                }

            }
        };

        mJoinGhour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailAndPassword();
            }
        });


        ArrayAdapter<String> workAdapter = new ArrayAdapter<String>(JoinActivity.this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.all_works)
        );
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(workAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectWhoAreYou = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: selectWhoYouAre=" + selectWhoAreYou);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void signInWithEmailAndPassword() {
        Log.d(TAG, "signInWithEmailAndPassword: click");
        String email = mUserEmailEditText.getText().toString();
        String password = mUserPasswordEditText.getText().toString();
        Log.d(TAG, "signInWithEmailAndPassword: email=" + email);
        Log.d(TAG, "signInWithEmailAndPassword: pass=" + password);
        if (!email.equals("") && !password.equals("")) {
            mAuthProgressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mAuthProgressDialog.dismiss();
                        checkUserExist();
                    } else {
                        mAuthProgressDialog.dismiss();
                        Toast.makeText(JoinActivity.this, "Error while Signin!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkUserExist() {
        String removeSpacesFromSelectedWorkName = selectWhoAreYou.replaceAll("\\s", "");
        String makeProperKeyForSelectedWorkName = removeSpacesFromSelectedWorkName.toLowerCase();
        Log.d(TAG, "createUserInFirebaseHelper: " + makeProperKeyForSelectedWorkName);

        final DatabaseReference mUseRef;


        if (mAuth.getCurrentUser() != null) {
            final String valid_user_id = mAuth.getCurrentUser().getUid();
            Log.d(TAG, "checkUserExist: user_id" + valid_user_id);

            if (makeProperKeyForSelectedWorkName.equals("user")) {
                mUseRef = mDatabase.child("user");
            } else {
                mUseRef = mDatabase.child("authority").child(makeProperKeyForSelectedWorkName);
            }

            mUseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(valid_user_id)) {
                        Intent singinIntent = new Intent(JoinActivity.this, MainActivity.class);
                        singinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(singinIntent);
                        finish();
                    } else {
                        Toast.makeText(JoinActivity.this, "You have to first setup your account!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void initializeScreen() {
        mUserEmailEditText = (EditText) findViewById(R.id.join_user_email);
        mUserPasswordEditText = (EditText) findViewById(R.id.join_user_password);
        mJoinGhour = (Button) findViewById(R.id.join_user_button);
        mSpinner = (Spinner) findViewById(R.id.join_select_your_work);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebas...");
        mAuthProgressDialog.setCancelable(false);


    }
}
