package com.example.jinming.gamestopdemo;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.util.Item;
import com.example.jinming.gamestopdemo.util.NaviAdapter;
import com.example.jinming.gamestopdemo.util.NonSwipeableViewPager;
import com.example.jinming.gamestopdemo.util.PagerAdapter;
import com.example.jinming.gamestopdemo.util.PlanetFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    NaviAdapter naviAdapter;
    ImageButton imageButton;
    ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    List<Item> list = Item.get();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /* **set the status color */
        Window window = this.getWindow();
    // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(onNaviButtonClickListener());


        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.naviListView);
        naviAdapter = new NaviAdapter(this, getApplicationContext(), list);
        drawerList.setAdapter(naviAdapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerList.setOnItemClickListener(DrawerListItemClickListener());
        drawerLayout.setDrawerListener(mDrawerToggle);
        //this line is used to remove the title in the tool bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private OnClickListener onNaviButtonClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.icon_placeholder) {
            return true;
        } else if (id == R.id.icon_cart) {
            return true;
        } else if (id == R.id.icon_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //click on the item of navi menu
    private ListView.OnItemClickListener DrawerListItemClickListener() {
        return new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        };
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        //Bundle args = new Bundle();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        Fragment fragment = new PlanetFragment();
        //fragment.setArguments(args);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack("planetFragment")
                .commit();
        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
//    }

    public void setContentFragment() {
        Fragment fragment = new ContentFragment();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                //.addToBackStack("myfragment")
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        FragmentManager fm = this.getSupportFragmentManager();
//        fm.popBackStack ("myfragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    }
}

