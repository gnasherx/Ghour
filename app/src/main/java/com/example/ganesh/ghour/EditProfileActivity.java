package com.example.ganesh.ghour;

import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";


    private Button mAddPeopleButton;

    //    Firebase Database
    private DatabaseReference mDatabasePeopleHelp;
    private FirebaseAuth mAuth;
    private String currentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeScreen();
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();
        mDatabasePeopleHelp = FirebaseDatabase.getInstance().getReference().child("addhelp").child(currentuser);


        mAddPeopleButton = (Button) findViewById(R.id.edit_profile_add_people_buttton);
        mAddPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPeople();
            }
        });
    }

    private void addPeople() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.add_people_dialog, null);
        final EditText nameEditText = (EditText) view.findViewById(R.id.people_dialog_name_editext);
        final EditText emailEdiText = (EditText) view.findViewById(R.id.people_dialog_email_editext);
        final EditText contactEditText = (EditText) view.findViewById(R.id.people_dialog_contact_editext);
        Button addPeopleButton = (Button) view.findViewById(R.id.add_people_dialog_button);

        addPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = (String) nameEditText.getText().toString();
                String email = (String) emailEdiText.getText().toString();
                String contact = (String) contactEditText.getText().toString();


                DatabaseReference addhelpRef=mDatabasePeopleHelp.push();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(contact)) {
                    addhelpRef.child("name").setValue(name);
                    addhelpRef.child("email").setValue(email);
                    addhelpRef.child("contact").setValue(contact);
                }

                nameEditText.setText("");
                emailEdiText.setText("");
                contactEditText.setText("");
                Toast.makeText(EditProfileActivity.this,"You successfully added help",Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
            }
        });


        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();




    }
}
