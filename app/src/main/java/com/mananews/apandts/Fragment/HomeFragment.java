package com.mananews.apandts.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.mananews.apandts.Activity.MainActivity;
import com.mananews.apandts.R;

public class HomeFragment extends Fragment {

    View view;
    String TAG = HomeFragment.class.getName();
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = (getActivity().getSupportFragmentManager());
        fragmentManager.beginTransaction().replace(R.id.lay_container, fragment).commit();

        Fragment statusfragment = new StatusFragment();
        FragmentManager statusfragmentManager = (getActivity().getSupportFragmentManager());
        statusfragmentManager.beginTransaction().replace(R.id.lay_status, statusfragment).commit();

        Fragment galleryfragment = new GalleryFragment();
        FragmentManager galleryfragmentManager = (getActivity().getSupportFragmentManager());
        galleryfragmentManager.beginTransaction().replace(R.id.lay_status, galleryfragment).commit();

        loadFragment(new MainFragment(), R.id.lay_container);

        TabHost tabhost = view.findViewById(R.id.tabhost);
        tabhost.setup();

        TabHost.TabSpec spec = tabhost.newTabSpec("News");
        spec.setContent(R.id.tab_news);
        spec.setIndicator("News");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("Status");
        spec.setContent(R.id.tab_status);
        spec.setIndicator("Status");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("Gallery");
        spec.setContent(R.id.tab_gallery);
        spec.setIndicator("Gallery");
        tabhost.addTab(spec);

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#000000")); // unselected
            TextView tv = tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        tabhost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff")); // selected
        TextView tv = tabhost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                Log.e(TAG, "onTabChanged: TabWidget count " + tabhost.getTabWidget().getChildCount());
                Log.e(TAG, "onTabChanged: tabId " + tabId);

                switch (tabId) {
                    case "News": {
                        tabhost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff")); // unselected
                        TextView tv = tabhost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
                        tv.setTextColor(Color.parseColor("#000000"));

                        tabhost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#000000")); // unselected
                        TextView tv1 = tabhost.getTabWidget().getChildAt(1).findViewById(android.R.id.title); //Unselected Tabs
                        tv1.setTextColor(Color.parseColor("#ffffff"));

                        tabhost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#000000")); // unselected
                        TextView tv2 = tabhost.getTabWidget().getChildAt(2).findViewById(android.R.id.title); //Unselected Tabs
                        tv2.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    }
                    case "Status": {
                        tabhost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#000000")); // unselected
                        TextView tv1 = tabhost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
                        tv1.setTextColor(Color.parseColor("#ffffff"));

                        tabhost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ffffff")); // unselected
                        TextView tv = tabhost.getTabWidget().getChildAt(1).findViewById(android.R.id.title); //Unselected Tabs
                        tv.setTextColor(Color.parseColor("#000000"));

                        tabhost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#000000")); // unselected
                        TextView tv2 = tabhost.getTabWidget().getChildAt(2).findViewById(android.R.id.title); //Unselected Tabs
                        tv2.setTextColor(Color.parseColor("#ffffff"));

                        loadFragment(statusfragment, R.id.lay_status);
                        break;
                    }
                    case "Gallery": {
                        tabhost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#000000")); // unselected
                        TextView tv1 = tabhost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
                        tv1.setTextColor(Color.parseColor("#ffffff"));

                        tabhost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#000000")); // unselected
                        TextView tv2 = tabhost.getTabWidget().getChildAt(1).findViewById(android.R.id.title); //Unselected Tabs
                        tv2.setTextColor(Color.parseColor("#ffffff"));

                        tabhost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#ffffff")); // unselected
                        TextView tv = tabhost.getTabWidget().getChildAt(2).findViewById(android.R.id.title); //Unselected Tabs
                        tv.setTextColor(Color.parseColor("#000000"));

                        loadFragment(new GalleryFragment(), R.id.lay_gallery);

                        break;
                    }
                }
            }
        });

        return view;
    }


    void loadFragment(Fragment fragment, int id) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }
}