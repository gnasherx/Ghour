package com.example.ganesh.ghour.viewpagers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.example.ganesh.ghour.R;
import com.example.ganesh.ghour.guideviewpager.AccidentFragment;
import com.example.ganesh.ghour.guideviewpager.EarthquakeFragment;
import com.example.ganesh.ghour.guideviewpager.FireFragment;
import com.example.ganesh.ghour.guideviewpager.FloodFragment;
import com.example.ganesh.ghour.guideviewpager.TsunamiFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {

    private String TAG="GuideFragment";

    public GuideFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_guide, container, false);

        ViewPager viewPager = (ViewPager)rootview.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout)rootview.findViewById(R.id.tab_layout);

//        creating sectionpager adapter

        SectionPagerAdapter adapter=new SectionPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);
        return rootview;
    }

    public class SectionPagerAdapter extends FragmentStatePagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = AccidentFragment.newInstance();
                    break;
                case 1:
                    fragment = FireFragment.newInstance();
                    break;
                case 2:
                    fragment = EarthquakeFragment.newInstance();
                    break;
                case 3:
                    fragment = TsunamiFragment.newInstance();
                    break;
                case 4:
                    fragment = FloodFragment.newInstance();
                    break;
                default:
                    fragment = AccidentFragment.newInstance();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {

            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.accident);
                case 1:
                    return getString(R.string.fire);
                case 2:
                    return getString(R.string.tsunami);
                case 3:
                    return getString(R.string.flood);
                case 4:
                    return getString(R.string.earthquake);
                default:
                    return getString(R.string.accident);
            }
        }
    }








}
