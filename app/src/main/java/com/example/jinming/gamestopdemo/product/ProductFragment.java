package com.example.jinming.gamestopdemo.product;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.MainActivity;
import com.example.jinming.gamestopdemo.R;
import com.example.jinming.gamestopdemo.cart.MyDialogFragment;
import com.example.jinming.gamestopdemo.network.ApiClient;
import com.example.jinming.gamestopdemo.network.ApiInterface;
import com.example.jinming.gamestopdemo.network.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment implements RecyclerViewClickListener {
    ProductAdapter adapter;
    List<CategoryItem> categoryItemList;
    List<SubCategoryItem> subCategoryList;
    List<ProductItem> productItemList;
    private ProgressDialog pDialog;
    RecyclerView recyclerView;
    ApiInterface apiInterface;
    SharedPreferences sp;
    Handler handler;
    Gson gson;
    UserInfo obj;
    FragmentManager fragmentManager;

    private int index;
    public ProductFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ProductFragment(int i, List<CategoryItem> categoryItemList, List<SubCategoryItem> subCategoryList, List<ProductItem> productItemList) {
        if (i == 1) {
            index = 1;
            this.categoryItemList = categoryItemList;
        } else if (i == 2) {
            index = 2;
            this.subCategoryList = subCategoryList;
        } else {
            index = 3;
            this.productItemList = productItemList;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product, container, false);
        TextView tv = view.findViewById(R.id.title);
        handler = new Handler();
        fragmentManager = getActivity().getSupportFragmentManager();
        init(view);
        if (index == 1) {
            tv.setText("This is Category List");
            setOnCategoryLayout();
        } else if (index == 2) {
            Bundle bundle = this.getArguments();
            String itemName = bundle.getString("itemName");
            tv.setText("This is " + itemName + " Category List");
            setOnSubCategoryLayout();
        } else if (index == 3) {
            Bundle bundle = this.getArguments();
            String itemName = bundle.getString("itemName");
            tv.setText("This is " + itemName + " Category List");
            setOnProductsLayout();
            setHasOptionsMenu(true);
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        gson = new Gson();
        String json = sp.getString("user", "");
        obj = gson.fromJson(json, UserInfo.class);
    }

    private void setOnCategoryLayout() {
        adapter = new ProductAdapter(getContext(), categoryItemList, null, null);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
    }

    private void setOnSubCategoryLayout() {
        adapter = new ProductAdapter(getContext(), null, subCategoryList, null);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
    }

    private void setOnProductsLayout() {
        adapter = new ProductAdapter(getContext(), null, null, productItemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        String apiKey = obj.getAppApiKey();
        String user_id = obj.getUserID();
        String itemId;
        if (categoryItemList != null) {
            String itemName = categoryItemList.get(position).getCatagoryName();
            itemId = categoryItemList.get(position).getId();
            getSubCategoryItem(itemId, apiKey, user_id);
            loadSubFromSharedPreference("subCategoryItemList", itemName);
        } else if (subCategoryList != null) {
            String itemName = subCategoryList.get(position).getSubCatagoryName();
            itemId = subCategoryList.get(position).getId();
            getProducts(itemId, apiKey, user_id);
            loadProductsFromSharedPreference("productItemList", itemName);
        } else {
            ProductItem item = productItemList.get(position);
Log.d("Products", " " + position);
            new MyDialogFragment(item).show(fragmentManager, "MyDialogFragment");
            //updateShoppingCart(item);
        }
    }


    private void getSubCategoryItem(String itemId, String apiKey, String user_id) {
        Call<SubCategory> subCategoryCall = apiInterface.getSubCategory(itemId, apiKey, user_id);
        subCategoryCall.enqueue(new Callback<SubCategory>() {
            @Override
            public void onResponse(Call<SubCategory> call, Response<SubCategory> response) {
                SubCategory res = response.body();
                List<SubCategoryItem> subCategoryItemList = res.getSubCategory();
                String list = gson.toJson(subCategoryItemList);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("subCategoryItemList", list).commit();
                //SubCategoryItem item = subCategoryItemList.get(0);
                //Log.d("onItemClick", item.getId());
            }
            @Override
            public void onFailure(Call<SubCategory> call, Throwable t) {
                Log.d("EOOR: ", "fail to get subCategory");
            }
        });
    }


    private void getProducts(String itemId, String apiKey, String user_id) {
        Call<Products> productsCall = apiInterface.getProducts(itemId, apiKey, user_id);
        productsCall.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Products res = response.body();
                List<ProductItem> productItemList = res.getProduct();
                SharedPreferences.Editor editor = sp.edit();
                String list = gson.toJson(productItemList);
                editor.putString("productItemList", list).commit();
                ProductItem item = productItemList.get(0);
                Log.d("PRODUCTS: ", item.getId());
            }
            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Log.d("EOOR: ", "fail to get subCategory");
            }
        });
    }

    private void loadSubFromSharedPreference(final String listIsGet, final String itemName) {
        showProgressDialog(listIsGet);
        String json = sp.getString(listIsGet, null);
        Type type = new TypeToken<ArrayList<SubCategoryItem>>() {}.getType();
        ArrayList<SubCategoryItem> list = gson.fromJson(json, type);
        ProductFragment fragment = new ProductFragment(2, null, list, null);
        Bundle bundle = new Bundle();
        bundle.putString("itemName", itemName);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack( "SubCategoryFragment" )
                .commit();
    }

    private void loadProductsFromSharedPreference(final String listIsGet, final String itemName) {
        showProgressDialog(listIsGet);
        String json = sp.getString(listIsGet, null);
        Type type = new TypeToken<ArrayList<ProductItem>>() {}.getType();
        ArrayList<ProductItem> list = gson.fromJson(json, type);
        ProductFragment fragment = new ProductFragment(3, null, null, list);
        Bundle bundle = new Bundle();
        bundle.putString("itemName", itemName);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack( "ProductItemFragment" )
                .commit();
    }

    private void showProgressDialog(String nameOfItem) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        while (!sp.contains(nameOfItem)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pDialog.setMessage("Loading...");
                }
            });
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
            }
        }, 1000);
    }

    private void updateShoppingCart(ProductItem item) {
        sp = getActivity().getSharedPreferences("CART", Context.MODE_PRIVATE);
        //cartList.add(item);
        Toast.makeText(getActivity(), item.getProductName() + " is added in cart", Toast.LENGTH_SHORT).show();
        int i = Integer.parseInt(MainActivity.tv.getText().toString());
        i++;
        //MainActivity.tv.setText(i);
        //getActivity().getActionView();
        //invalidateOptionsMenu();
    }
}
