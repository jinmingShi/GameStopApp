package com.example.jinming.gamestopdemo;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {
    private static final int NUM_PAGES = 7;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    TabLayout tabLayout;

    public TabFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), NUM_PAGES);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        for(int i = 0; i < tabLayout.getTabCount() - 1; i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 15, 0);
            tab.requestLayout();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        int numOfTabs;

        public ScreenSlidePagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    SlidePageFragment tab0 = new SlidePageFragment(0);
                    return tab0;
                case 1:
                    SlidePageFragment tab1 = new SlidePageFragment(1);
                    return tab1;
                case 2:
                    SlidePageFragment tab2 = new SlidePageFragment(2);
                    return tab2;
                case 3:
                    SlidePageFragment tab3 = new SlidePageFragment(3);
                    return tab3;
                case 4:
                    SlidePageFragment tab4 = new SlidePageFragment(4);
                    return tab4;
                case 5:
                    SlidePageFragment tab5 = new SlidePageFragment(5);
                    return tab5;
                case 6:
                    SlidePageFragment tab6 = new SlidePageFragment(6);
                    return tab6;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


//    private void updateButtonText(final Button btn) {
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                btn.setText(R.string.btn_find);
//            }
//        });
//    }
//
//    private void runThread(final Button btn) {
//        new Thread() {
//            public void run() {
//                int i = 0;
//                while (i++ < 1000) {
//                    try {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                btn.setText(R.string.btn_find);
//                            }
//                        });
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
}
