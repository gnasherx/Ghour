package com.example.ganesh.ghour.guideviewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ganesh.ghour.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccidentFragment extends Fragment {


    public AccidentFragment() {
        // Required empty public constructor
    }

    public static AccidentFragment newInstance() {
        AccidentFragment accidentFragment=new AccidentFragment();
        Bundle args=new Bundle();
        accidentFragment.setArguments(args);
        return accidentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accident, container, false);
    }

}
