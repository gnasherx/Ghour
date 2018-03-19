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
public class FireFragment extends Fragment {


    public FireFragment() {
        // Required empty public constructor
    }

    public static FireFragment newInstance() {
        FireFragment fireFragment = new FireFragment();
        Bundle bundle = new Bundle();
        fireFragment.setArguments(bundle);
        return fireFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fire, container, false);
    }

}
