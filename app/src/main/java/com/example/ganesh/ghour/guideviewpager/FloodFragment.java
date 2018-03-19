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
public class FloodFragment extends Fragment {


    public FloodFragment() {
        // Required empty public constructor
    }

    public static FloodFragment newInstance() {
        FloodFragment floodFragment = new FloodFragment();
        Bundle bundle = new Bundle();
        floodFragment.setArguments(bundle);
        return floodFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flood, container, false);
    }

}
