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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ganesh.ghour.MainActivity;
import com.example.ganesh.ghour.R;
import com.example.ganesh.ghour.authentication.create.imuser.PasswordFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorityNameFragment extends Fragment  implements FragmentManager.OnBackStackChangedListener {
    private final String TAG = "AuthorityNameFragment";
    private EditText mAuthorityNameEditText;
    private Button mAuthorityGoForPassword;
    private Spinner mAuthoritySelectYourWork;


    public AuthorityNameFragment() {
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
        View rootview= inflater.inflate(R.layout.fragment_authority_name, container, false);
        mAuthorityNameEditText=(EditText)rootview.findViewById(R.id.fragment_signup_authroity_name);
        mAuthorityGoForPassword=(Button)rootview.findViewById(R.id.fragment_signup_go_for_password);
        mAuthoritySelectYourWork=(Spinner)rootview.findViewById(R.id.authority_select_your_work);

        mAuthorityNameEditText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        mAuthorityNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence email, int start, int before, int count) {
                if (email.toString().trim().length() == 0) {
                    mAuthorityGoForPassword.setBackgroundResource(R.drawable.signup_name_disabled);
                    mAuthorityGoForPassword.setEnabled(false);
                } else {
                    mAuthorityGoForPassword.setBackgroundResource(R.drawable.signup_name);
                    mAuthorityGoForPassword.setEnabled(true);
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


        if (mAuthorityGoForPassword.isEnabled()) {
            mAuthorityGoForPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorityPasswordFragment passwordFragment = new AuthorityPasswordFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.authority_name_fragment, passwordFragment, "AuthorityPassword");
                    ft.addToBackStack("Add");
                    ft.commit();

                }
            });
        }


        ArrayAdapter<String> workAdapter=new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.works)
        );
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAuthoritySelectYourWork.setAdapter(workAdapter);

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
