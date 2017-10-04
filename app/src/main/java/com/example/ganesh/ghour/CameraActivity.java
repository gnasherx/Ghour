package com.example.ganesh.ghour;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.ganesh.ghour.R.id.linerlayout;

public class CameraActivity extends AppCompatActivity {

    //variables
    private EditText mIncidentDescriptionEditText;
    private Button mPostThisIncidentDetailsButton;
    private LinearLayout linearLayout;
    private TextView mIncidentNameTextView;


    //    Firebase Database
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private String currentuser;
    private ProgressDialog mAuthProgessDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initializeScreen();
        mPostThisIncidentDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postThisPicture();
            }
        });


    }


    private void initializeScreen() {
        //varibles
        mIncidentDescriptionEditText = (EditText) findViewById(R.id.camera_edit_text);
        mPostThisIncidentDetailsButton = (Button) findViewById(R.id.camera_post_picture_button);
        linearLayout = (LinearLayout) findViewById(linerlayout);
        mIncidentNameTextView=(TextView)findViewById(R.id.camera_incident_name_textview);

        mAuthProgessDialog = new ProgressDialog(this);
        mAuthProgessDialog.setTitle("Uploading");
        mAuthProgessDialog.setMessage("Your Incident details are uploading");
        mAuthProgessDialog.setCancelable(false);

//        firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("incident");
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser);

    }

    private void postThisPicture() {


        String incidentdescription = mIncidentDescriptionEditText.getText().toString();
        if (!TextUtils.isEmpty(incidentdescription)) {
            mAuthProgessDialog.show();

            final DatabaseReference newIncidentRef = mDatabase.push();

//                  uplaoading picture to firebase with user details
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newIncidentRef.child("uid").setValue(currentuser);
                    newIncidentRef.child("details").setValue(mIncidentDescriptionEditText.getText().toString());
                    newIncidentRef.child("name").setValue(dataSnapshot.child("name").getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mAuthProgessDialog.dismiss();
                            Snackbar.make(linearLayout, "Incident picture uploaded successfully", Snackbar.LENGTH_LONG).show();
                            mIncidentDescriptionEditText.setText("");

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mAuthProgessDialog.dismiss();
                    Toast.makeText(CameraActivity.this, "Uploading failed!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}
