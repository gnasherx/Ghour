package com.example.ganesh.ghour.authentication.create.iamauthority;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.ganesh.ghour.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorityEmailFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private final String TAG = "AuthorityEmail";


    private EditText mAuthorityEmailEditText;
    private Button mAuthoritySignupButton;

    public AuthorityEmailFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_authority_email, container, false);
        mAuthorityEmailEditText = (EditText) rootview.findViewById(R.id.fragment_signup_authority_email);
        mAuthoritySignupButton = (Button) rootview.findViewById(R.id.fragment_signup_button);

        mAuthorityEmailEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mAuthorityEmailEditText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        mAuthorityEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence email, int start, int before, int count) {
                if (email.toString().trim().length() == 0) {
                    mAuthoritySignupButton.setBackgroundResource(R.drawable.signup_button_disabled);
                    mAuthoritySignupButton.setEnabled(false);
                } else {
                    mAuthoritySignupButton.setBackgroundResource(R.drawable.signup_button);
                    mAuthoritySignupButton.setEnabled(true);
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


        if (mAuthoritySignupButton.isEnabled()) {
            mAuthoritySignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorityNameFragment nameFragment = new AuthorityNameFragment();
                    Bundle args = new Bundle();
                    args.putString("authorityemail", mAuthorityEmailEditText.getText().toString());
                    nameFragment.setArguments(args);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.authority_email_fragment, nameFragment, "AuthorityName");
                    ft.addToBackStack("Add");
                    ft.commit();

                }
            });
        }


        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF1F8E62"));
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "onCreateView: color changing");
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#1976D2"));
        }
        hideKeyboard(getActivity());
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack("AuthorityName", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
