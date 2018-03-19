package com.example.ganesh.ghour.authentication.create.imuser;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
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
public class NameFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private final String TAG = "NameFragment";
    private EditText mUserNameEditText;
    private Button mUserGoForPasswordButton;
    private String useremail;

    public NameFragment() {
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

        View rootview = inflater.inflate(R.layout.fragment_name, container, false);
        mUserNameEditText = (EditText) rootview.findViewById(R.id.fragment_signup_user_name);
        mUserGoForPasswordButton = (Button) rootview.findViewById(R.id.fragment_signup_go_for_password);

        useremail = getArguments().getString("useremail");

        mUserNameEditText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence email, int start, int before, int count) {
                if (email.toString().trim().length() == 0) {
                    mUserGoForPasswordButton.setBackgroundResource(R.drawable.signup_name_disabled);
                    mUserGoForPasswordButton.setEnabled(false);
                } else {
                    mUserGoForPasswordButton.setBackgroundResource(R.drawable.signup_name);
                    mUserGoForPasswordButton.setEnabled(true);
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


        if (mUserGoForPasswordButton.isEnabled()) {
            mUserGoForPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PasswordFragment passwordFragment = new PasswordFragment();
                    Bundle args = new Bundle();
                    args.putString("username", (mUserNameEditText.getText().toString()) + "/" + useremail);
                    passwordFragment.setArguments(args);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.user_name_fragment, passwordFragment, "AuthorityPassword");
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
            window.setStatusBarColor(Color.parseColor("#FFCE8B3A"));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF1F8E62"));
        }
        hideKeyboard(getActivity());
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack("AuthorityPassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
