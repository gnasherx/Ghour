package com.example.ganesh.ghour.guideviewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.TextViewCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ganesh.ghour.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakeFragment extends Fragment {


    private TextView mWhatToDoTextview;
    public EarthquakeFragment() {
        // Required empty public constructor
    }

    public static  EarthquakeFragment newInstance() {
        EarthquakeFragment earthquakeFragment=new EarthquakeFragment();
        Bundle bundle=new Bundle();
        earthquakeFragment.setArguments(bundle);
        return earthquakeFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_earthquake, container, false);
        mWhatToDoTextview=(TextView)rootview.findViewById(R.id.what_to_do_textview);

        return rootview;
    }

}
