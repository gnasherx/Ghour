package com.example.ganesh.ghour.viewpagers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ganesh.ghour.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {


    public GuideFragment() {
        // Required empty public constructor
    }

    public static GuideFragment newInstance(){
        GuideFragment guideFragment=new GuideFragment();
        Bundle args=new Bundle();
        guideFragment.setArguments(args);
        return guideFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

}
