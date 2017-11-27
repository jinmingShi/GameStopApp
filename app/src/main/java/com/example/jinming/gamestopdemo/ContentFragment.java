package com.example.jinming.gamestopdemo;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinming.gamestopdemo.util.NonSwipeableViewPager;
import com.example.jinming.gamestopdemo.util.PagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {
    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;
    PagerAdapter adapter;

    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewPager);
        setViewPager();
        setTabLayout();
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void setTabLayout() {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TextView tab1 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null).findViewById(R.id.tabContent);
        tab1.setText("HOME");
        tab1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hangar, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tab1);

        TextView tab2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null).findViewById(R.id.tabContent);
        tab2.setText("TRADE");
        tab2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trade, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tab2);;

        TextView tab3 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null).findViewById(R.id.tabContent);
        tab3.setText("SHOP");
        tab3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shoppingbag, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tab3);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setViewPager() {
        Log.d("VIEWPAGER", "setViewPager");
        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

}
