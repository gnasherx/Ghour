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
public class TsunamiFragment extends Fragment {


    public TsunamiFragment() {
        // Required empty public constructor
    }

    public static TsunamiFragment newInstance() {
        TsunamiFragment tsunamiFragment=new TsunamiFragment();
        Bundle bundle=new Bundle();
        tsunamiFragment.setArguments(bundle);
        return tsunamiFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tsunami, container, false);
    }

}
