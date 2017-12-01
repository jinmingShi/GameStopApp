package com.example.jinming.gamestopdemo.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private static final String KEY = "user";
    EditText mobile;
    EditText password;
    Button login;
    Button signon;
    SharedPreferences sp;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Unbinder unbinder;
    Toolbar mActionBarToolbar;
    Handler handler;
    FragmentManager fragmentManager;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        handler = new Handler();
        mobile = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        fragmentManager = getActivity().getSupportFragmentManager();
        login = view.findViewById(R.id.login);
        signon = view.findViewById(R.id.signon);
        unbinder = ButterKnife.bind(this, view);
        sp = getActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        mActionBarToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        return view;
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.login, R.id.signon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                loginClickListener(view);
                break;
            case R.id.signon:
                registerClickListener(view);
                break;
        }
    }

    private void loginClickListener(View view) {
        if (!TextUtils.isEmpty(mobile.getText()) && !TextUtils.isEmpty(password.getText())) {
            String input_mobile = mobile.getText().toString();
            String input_password = password.getText().toString();
            Call<List<UserInfo>> userInfoCall = apiInterface.getUserInfo(input_mobile, input_password);
            userInfoCall.enqueue(new Callback<List<UserInfo>>() {
                @Override
                public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                    UserInfo obj = response.body().get(0);
                    Gson gson = new Gson();
                    String userInfo = gson.toJson(obj);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(KEY, userInfo).commit();
                    getCategoryList(obj.getAppApiKey(), obj.getUserID());

                    showProgressDialog(1);
                    Log.d("here Login ", "Success");
                }
                @Override
                public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Password is incorrect, please register first", Toast.LENGTH_SHORT).show();
                    Log.d("here Login  ", "Fail");
                }
            });
            //showProgressDialog(KEY);

//            Gson gson = new Gson();
//            UserInfo userInfo = gson.fromJson(sp.getString(KEY, null), UserInfo.class);

        } else {
            Toast.makeText(getActivity(), "Please fill in mobile number or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog(String nameOfItem) {
        int i = 50;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("login");
        pDialog.setCancelable(false);
        pDialog.show();
        while (!sp.contains(nameOfItem) && i > 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pDialog.setMessage("login...");
                }
            });
            i--;
            Log.d("number: ", i + "");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
            }
        }, 1000);
    }

    private void registerClickListener(View view) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, new RegisterFragment()).addToBackStack("RegisterFragment").commit();
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
        Gson gson = new Gson();
        ArrayList<CategoryItem> itemList = gson.fromJson(json, type);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, new ProductFragment(n, itemList, null, null))
                .commit();
    }

}
