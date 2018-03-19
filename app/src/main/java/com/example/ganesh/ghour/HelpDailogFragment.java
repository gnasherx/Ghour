package com.example.ganesh.ghour;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ganesh.ghour.viewpagers.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDailogFragment extends DialogFragment {

    private static  final String TAG="HelpDialogFragment";

    private DatabaseReference mDatabaseHelpCountRef;
    private FirebaseAuth mAuth;
    String currentuser;
    String incidentKey;

    public HelpDailogFragment() {
        // Required empty public constructor
    }

    public HelpDailogFragment newInstance() {
        // Required empty public constructor

        HelpDailogFragment helpDailogFragment = new HelpDailogFragment();
        return helpDailogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_help_dailog, container, false);
        HomeFragment homefragment=new HomeFragment();
        incidentKey = homefragment.getIncidentKey();
        Log.d(TAG, "onCreateView: incidentkey="+incidentKey);
//        incidentKey = getArguments().getString("incidentkey");

        //      firebase
        mDatabaseHelpCountRef = FirebaseDatabase.getInstance().getReference().child("helpcount");
        mDatabaseHelpCountRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser().getUid();





        return rootview;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View rootview=inflater.inflate(R.layout.single_help_dialog,null);

        builder.setTitle("Help").setView(rootview)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addHelpCount();
                    }
                })
                .setNegativeButton("No",null);
        setCancelable(true);
        return builder.create();

    }

    private void addHelpCount() {

        mDatabaseHelpCountRef.child(incidentKey).push().setValue(currentuser);
        HelpDailogFragment.this.getDialog().cancel();
        Toast.makeText(getActivity(),"You are going",Toast.LENGTH_SHORT).show();
    }
}