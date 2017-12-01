package com.example.jinming.gamestopdemo.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import com.example.jinming.gamestopdemo.TabFragment1;
import com.example.jinming.gamestopdemo.TabFragment2;
import com.example.jinming.gamestopdemo.TabFragment3;

/**
 * Created by Jinming on 11/13/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    FragmentManager fragmentManager;
    View view;
    public PagerAdapter(FragmentManager fm, int n, View view) {
        super(fm);
        this.numOfTabs = n;
        this.fragmentManager = fm;
        this.view = view;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                Log.d("FRAGMENT", "tab1");
                TabFragment1 tab1 = new TabFragment1();
                //fragmentManager.beginTransaction().add(tab1, "tab1").addToBackStack("TabFragment1").commit();
                return tab1;
            case 1:
                Log.d("FRAGMENT", "tab2");
//                if (fragmentManager.findFragmentByTag("tab2") == null) {
//                    fragmentManager.beginTransaction().add(new TabFragment2(), "tab2").commit();
//                }
//                TabFragment2 tab2 = (TabFragment2) fragmentManager.findFragmentByTag("tab2");
                TabFragment2 tab2 = new TabFragment2();
                //fragmentManager.beginTransaction().add(tab2, "tab2").addToBackStack("TabFragment2").commit();
                return tab2;
            case 2:
                Log.d("FRAGMENT", "tab3");
//                if (fragmentManager.findFragmentByTag("tab3") == null) {
//                    fragmentManager.beginTransaction().add(new TabFragment3(), "tab3").commit();
//                }
//                TabFragment3 tab3 = (TabFragment3) fragmentManager.findFragmentByTag("tab3");
                TabFragment3 tab3 = new TabFragment3();
                //fragmentManager.beginTransaction().add(tab3, "tab3").addToBackStack("TabFragment3").commit();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Tab-1";
        } else if (position == 1) {
            title = "Tab-2";
        } else {
            title = "Tab-3";
        }
        return title;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
