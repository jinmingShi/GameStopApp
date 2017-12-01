package com.example.jinming.gamestopdemo;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.login.LoginFragment;
import com.example.jinming.gamestopdemo.cart.CartFragment;
import com.example.jinming.gamestopdemo.cart.CartItem;
import com.example.jinming.gamestopdemo.network.ApiClient;
import com.example.jinming.gamestopdemo.network.ApiInterface;
import com.example.jinming.gamestopdemo.network.UserInfo;
import com.example.jinming.gamestopdemo.product.Category;
import com.example.jinming.gamestopdemo.product.CategoryItem;
import com.example.jinming.gamestopdemo.product.ProductFragment;
import com.example.jinming.gamestopdemo.util.Item;
import com.example.jinming.gamestopdemo.util.NaviAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String KEY = "user";

    private ApiInterface apiInterface;
    private ProgressDialog pDialog;
    private Handler handler;
    private Toolbar toolbar;
    private NaviAdapter naviAdapter;
    private ImageButton imageButton;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    public static TextView tv;
    private FragmentManager fragmentManager;
    private SharedPreferences sp;
    private List<Item> naviList = Item.get();
    public static List<CartItem> cartItems;

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
        cartItems = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(onNaviButtonClickListener());
        sp = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        handler = new Handler();
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.naviListView);
        naviAdapter = new NaviAdapter(this, getApplicationContext(), naviList);
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
        setContentFragment();
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
        return super.onCreateOptionsMenu(menu);
//        MenuItem item = menu.findItem(R.id.icon_cart);
//        MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
//        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
//        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
//        //SharedPreferences.Editor editor = sp.edit();
//        tv.setText("0");
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                LayoutInflater inflater = getLayoutInflater();
//                View layout = inflater.inflate(R.layout.fragment3_list_item, (ViewGroup) findViewById(R.id.container));
//
//                TextView text = (TextView) layout.findViewById(R.id.textView1);
//                text.setText("This is a custom toast, sjfjdslf jslfdj slkdjf");
//                text.setTextColor(Color.BLUE);
//
//                Toast toast = new Toast(getApplicationContext());
//                toast.setView(layout);
//                toast.show();
//                return true;
//            }
//        });
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.icon_cart).getActionView();
//        TextView tv = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
//        tv.setText("12");
//        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MENU: ", "click");
        int id = item.getItemId();
        if (id == R.id.icon_placeholder) {
            return true;
        } else if (id == R.id.icon_cart) {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Empty Cart, please select product first", Toast.LENGTH_SHORT).show();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, new CartFragment())
                        .addToBackStack("CartFragment")
                        .commit();
            }
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
        //args.putInt(LoginFragment.ARG_PLANET_NUMBER, position);
        Fragment fragment;
        //FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                Log.d("NVAI_ITEM", position + "");
                if (sp.getString(KEY, null) != null) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(sp.getString(KEY, null), UserInfo.class);
                    getCategoryList(userInfo.getAppApiKey(), userInfo.getUserID());
                    showProgressDialog(1);
                } else {
                    fragment = new LoginFragment();
                    //fragment.setArguments(args);
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .addToBackStack("LoginFragment")
                            .commit();
                }
                break;
            case 1:
                Log.d("NVAI_ITEM", position + "");
                fragment = new LoginFragment();
                //fragment.setArguments(args);
                // Insert the fragment by replacing any existing fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .addToBackStack("LoginFragment")
                        .commit();
                break;
            case 2:
                Log.d("NVAI_ITEM", position + "");
                fragment = new LoginFragment();
                //fragment.setArguments(args);
                // Insert the fragment by replacing any existing fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .addToBackStack("LoginFragment")
                        .commit();
                break;
            case 3:
                Log.d("NVAI_ITEM", position + "");
                fragment = new LoginFragment();
                //fragment.setArguments(args);
                // Insert the fragment by replacing any existing fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .addToBackStack("LoginFragment")
                        .commit();
                break;
            case 4:
                Log.d("NVAI_ITEM", position + "");
                fragment = new LoginFragment();
                //fragment.setArguments(args);
                // Insert the fragment by replacing any existing fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .addToBackStack("LoginFragment")
                        .commit();
                break;
            case 5:
                break;
            case 6:
                Log.d("NVAI_ITEM", position + "");
                logout();
                break;
            default:
                Log.d("NVAI_ITEM", position + "");
                fragment = new ContentFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .addToBackStack("ContentFragment")
                        .commit();
                break;
        }
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

    private void logout() {
        SharedPreferences.Editor editor = sp.edit();
        if (sp.contains(KEY)) {
            sp.edit().remove(KEY).commit();
            Toast.makeText(this, "Successful logout", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Already logout", Toast.LENGTH_SHORT).show();
        }
        setContentFragment();
    }

    public void setContentFragment() {
        Fragment fragment = new ContentFragment();
        //args.putInt(LoginFragment.ARG_PLANET_NUMBER, position);
        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack("myfragment")
                .commit();
    }

    private void getCategoryList(String apiKey, String id) {
        Call<Category> userInfoCall = apiInterface.getCatrgory(apiKey, id);
        userInfoCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Category category = response.body();
                List<CategoryItem> list = category.getCategory();
                SharedPreferences.Editor editor = sp.edit();
                Gson gson = new Gson();
                String itemlist = gson.toJson(list);
                editor.putString("itemList", itemlist).commit();
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.d("TAG: ", " getCategoryList finish");
            }
        });
    }

    private void showProgressDialog(int n) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Category");
        pDialog.setCancelable(false);
        pDialog.show();
        while (!sp.contains("itemList")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pDialog.setMessage("Loading Category");
                }
            });
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
            }
        }, 1000);

        String json = sp.getString("itemList", null);
        Type type = new TypeToken<ArrayList<CategoryItem>>() {}.getType();
        Gson gson = new Gson();
        ArrayList<CategoryItem> itemList = gson.fromJson(json, type);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, new ProductFragment(n, itemList, null, null))
                .commit();
    }
//    @Override
//    public void onBackPressed() {
//        FragmentManager fm = this.getSupportFragmentManager();
//        fm.popBackStack ("myfragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    }
}

