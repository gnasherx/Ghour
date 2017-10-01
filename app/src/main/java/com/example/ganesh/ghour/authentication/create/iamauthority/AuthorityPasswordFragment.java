package com.example.ganesh.ghour.authentication.create.iamauthority;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ganesh.ghour.MainActivity;
import com.example.ganesh.ghour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorityPasswordFragment extends Fragment {
    private final String TAG = "AuthorityPasswordFragment";
    private EditText mAuthorityPasswordEditText;
    private Button mAuthorityGetStartedButton;
    private String password, name, email, selectedwork;
    private ProgressDialog mAuthProgessDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public AuthorityPasswordFragment() {
        // Required empty public constructor
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_authority_password, container, false);
        mAuthorityPasswordEditText = (EditText) rootview.findViewById(R.id.fragment_signup_authority_password);
        mAuthorityGetStartedButton = (Button) rootview.findViewById(R.id.fragment_signup_get_started);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ghourUsers");
        mAuth = FirebaseAuth.getInstance();

        mAuthProgessDialog = new ProgressDialog(getContext());
        mAuthProgessDialog.setTitle("Loading...");
        mAuthProgessDialog.setMessage("Attempting to create account...");
        mAuthProgessDialog.setCancelable(false);


        mAuthorityPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());

        mAuthorityPasswordEditText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        mAuthorityPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence email, int start, int before, int count) {
                if (email.toString().trim().length() <= 6) {
                    mAuthorityGetStartedButton.setBackgroundResource(R.drawable.signup_name_disabled);
                    mAuthorityGetStartedButton.setEnabled(false);
                } else {
                    mAuthorityGetStartedButton.setBackgroundResource(R.drawable.signup_name);
                    mAuthorityGetStartedButton.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        if (mAuthorityGetStartedButton.isEnabled()) {
            mAuthorityGetStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        password = getArguments().getString("authorityname");
        String string = password;
        String[] parts = string.split("/");
        email = parts[0];
        name = parts[1];
        selectedwork = parts[2];


        if (mAuthorityGetStartedButton.isEnabled()) {
            mAuthorityGetStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewAuthorityUser();
                }
            });
        }


        return rootview;
    }

    private void createNewAuthorityUser() {
        mAuthProgessDialog.show();
        Log.d(TAG, "createNewAuthorityUser: email=" + email);
        Log.d(TAG, "createNewAuthorityUser: pass=" + mAuthorityPasswordEditText.getText().toString());
        mAuth.createUserWithEmailAndPassword(email, mAuthorityPasswordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuthProgessDialog.dismiss();
                    String user_id = mAuth.getCurrentUser().getUid();

                    createUserInFirebaseHelper(user_id);
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    Toast.makeText(getContext(), "Register SuccessFully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onComplete: task is unsuccessfull!");
                }
            }
        });
    }

    private void createUserInFirebaseHelper(String user_id) {

        String removeSpacesFromSelectedWorkName = selectedwork.replaceAll("\\s", "");
        String makeProperKeyForSelectedWorkName = removeSpacesFromSelectedWorkName.toLowerCase();
        Log.d(TAG, "createUserInFirebaseHelper: " + makeProperKeyForSelectedWorkName);

        final DatabaseReference userLocationReference = mDatabase.child("authority").child(makeProperKeyForSelectedWorkName).child(user_id);
        userLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLocationReference.child("email").setValue(email);
                userLocationReference.child("name").setValue(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error While Registration", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FFE13B5A"));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FFCE8B3A"));
        }

        hideKeyboard(getActivity());
    }

}
