package com.example.ganesh.ghour.authentication.create.imuser;


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
public class PasswordFragment extends Fragment {
    private final String TAG = "PasswordFragment";
    private EditText mUserPasswordEditText;
    private Button mUserGetStartedButton;
    private String username, name, email;
    private ProgressDialog mAuthProgessDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public PasswordFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_password, container, false);
        mUserPasswordEditText = (EditText) rootview.findViewById(R.id.fragment_signup_user_password);
        mUserGetStartedButton = (Button) rootview.findViewById(R.id.fragment_signup_get_started);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();


        mAuthProgessDialog = new ProgressDialog(getContext());
        mAuthProgessDialog.setTitle("Loading...");
        mAuthProgessDialog.setMessage("Attempting to create account...");
        mAuthProgessDialog.setCancelable(false);

        mUserPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());


        mUserPasswordEditText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        mUserPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence email, int start, int before, int count) {
                if (email.toString().trim().length() <= 6) {
                    mUserGetStartedButton.setBackgroundResource(R.drawable.signup_name_disabled);
                    mUserGetStartedButton.setEnabled(false);
                } else {
                    mUserGetStartedButton.setBackgroundResource(R.drawable.signup_name);
                    mUserGetStartedButton.setEnabled(true);
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


        username = getArguments().getString("username");
        String string = username;
        String[] parts = string.split("/");
        name = parts[0];
        email = parts[1];

//       firebase database connection
        if (mUserGetStartedButton.isEnabled()) {
            mUserGetStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewUser();

                }
            });
        }
        return rootview;
    }

    private void createNewUser() {
        mAuthProgessDialog.show();
        mAuth.createUserWithEmailAndPassword(email, mUserPasswordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuthProgessDialog.dismiss();
                    String user_id = mAuth.getCurrentUser().getUid();

                    createUserInFirebaseHelper(user_id);
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    Toast.makeText(getContext(), "Register SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUserInFirebaseHelper(final String user_id) {
        final DatabaseReference userLocationReference = mDatabase.child(user_id);
        userLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLocationReference.child("email").setValue(email);
                userLocationReference.child("name").setValue(name);
                userLocationReference.child("work").setValue("User");
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
