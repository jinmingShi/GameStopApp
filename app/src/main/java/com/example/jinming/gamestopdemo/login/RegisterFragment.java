package com.example.jinming.gamestopdemo.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.R;
import com.example.jinming.gamestopdemo.network.ApiClient;
import com.example.jinming.gamestopdemo.network.ApiInterface;
import com.example.jinming.gamestopdemo.network.UserInfo;
import com.example.jinming.gamestopdemo.product.Category;
import com.example.jinming.gamestopdemo.product.CategoryItem;
import com.example.jinming.gamestopdemo.product.ProductFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    ApiInterface apiInterface;
    SharedPreferences sp;
    ProgressDialog pDialog;
    Handler handler;
    Gson gson;
    FragmentManager fragmentManager;
    Toolbar mActionBarToolbar;

    @BindView(R.id.register)
    Button register;
    @BindView(R.id.editTextRname)
    EditText editTextRname;
    @BindView(R.id.editTextRemail)
    EditText editTextRemail;
    @BindView(R.id.editTextRmobile)
    EditText editTextRmobile;
    @BindView(R.id.editTextRpassword)
    EditText editTextRpassword;

    Unbinder unbinder;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        handler = new Handler();
        gson = new Gson();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        unbinder = ButterKnife.bind(this, view);
        fragmentManager = getActivity().getSupportFragmentManager();
        sp = getActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        mActionBarToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        return view;
    }

    @OnClick(R.id.register)
    public void onViewClicked() {
        registerClickListener(getView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void registerClickListener(View view) {
                                                                        //Log.d("signon", " 1");
        final String name = editTextRname.getText().toString();
        final String email = editTextRemail.getText().toString();
        final String mobile = editTextRmobile.getText().toString();
        final String password = editTextRpassword.getText().toString();
        if (name == null || email == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || mobile == null || password == null) {
                                                                            //Invalid input
            makeToast("Please check the input");
        } else {
                                                            // Make a request to GET the account registered.
            Call<String> registerUserInfoCall = apiInterface.registerUserInfo(name, email, mobile, password);
            registerUserInfoCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                                            // GET successfully and to get a Userinfo object, pass it into sharedpreferences;
                    getUserInfo(mobile, password);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    makeToast("Fail to register!");
                }
            });
        }
    }

    public void onResume(){
        super.onResume();
    }

    private void getUserInfo(final String mobile, final String password) {
        Call<List<UserInfo>> userInfoCall = apiInterface.getUserInfo(mobile, password);
        userInfoCall.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                                                                    // GET userInfo after successful register
                UserInfo userInfo = response.body().get(0);
                final String id = userInfo.getUserID();
                final String apiKey = userInfo.getAppApiKey();
                                                                // transfer java object to Json string
                String json = gson.toJson(userInfo);
                                                                // Put userInfo in the SharedPreference
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user", json);
                editor.putString("password", password);
                editor.commit();
                                            //                Log.d("GET USER: ", "success");
                                            //                Log.d("INFO: ", id + " " + apiKey);
                getCategoryList(apiKey, id);
                showProgressDialog(1);
            }
            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d("GET USER: ", "fail");
            }
        });
    }

    private void getCategoryList(String apiKey, String id) {
        Call<Category> userInfoCall = apiInterface.getCatrgory(apiKey, id);
        userInfoCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Category category = response.body();
                List<CategoryItem> list = category.getCategory();
                SharedPreferences.Editor editor = sp.edit();
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
        pDialog = new ProgressDialog(getActivity());
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
        ArrayList<CategoryItem> itemList = gson.fromJson(json, type);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, new ProductFragment(n, itemList, null, null))
                .commit();
    }

    private void makeToast(String str) {
        if (str == null) {
            Log.d("TOAST_ERROR: ", "null input");
            return;
        }
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
}
