package com.example.jinming.gamestopdemo.cart;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.MainActivity;
import com.example.jinming.gamestopdemo.network.ApiClient;
import com.example.jinming.gamestopdemo.network.ApiInterface;
import com.example.jinming.gamestopdemo.network.UserInfo;

import com.example.jinming.gamestopdemo.product.DividerItemDecoration;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.example.jinming.gamestopdemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener {
    public static final int PAYPAL_REQUEST_CODE = 123;
    SharedPreferences sp;
    private static PayPalConfiguration payPalConfiguration;
    private RecyclerView recyclerView;
    private Button btnCheckout, btnClear;
    private CartItemAdapter adapter;
    //private List<CartItem> cartItems;
    private TextView tvHint, tvTotal;
    private int total;
    Gson gson;
    private UserInfo userInfo;
    private CartItem cartItem;
    private ApiInterface apiInterface;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        tvHint = view.findViewById(R.id.tvHint);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        sp = getActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        gson = new Gson();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if(MainActivity.cartItems.size() == 0) tvHint.setVisibility(View.VISIBLE);

        showTotalAmount();
        return view;
    }

    private void showTotalAmount() {
        total = 0;
        for (int i = 0; i < MainActivity.cartItems.size(); i++){
            total += Integer.valueOf(MainActivity.cartItems.get(i).getQuantity())*
                    Integer.valueOf(MainActivity.cartItems.get(i).getProductItem().getPrize());
        }
        tvTotal.setText("Total amount: $" + total);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new CartItemAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckout:
                if (MainActivity.cartItems.size() == 0) {
                    Toast.makeText(getActivity(), "Please add items to your cart first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getPayment();
                showTotalAmount();
                break;
            case R.id.btnClear:
                MainActivity.cartItems.clear();
                adapter.notifyDataSetChanged();
                showTotalAmount();
                Toast.makeText(getActivity(), "Your cart is cleared!", Toast.LENGTH_SHORT).show();
                if (MainActivity.cartItems.size() == 0) tvHint.setVisibility(View.VISIBLE);
        }
    }

    private void getPayment() {
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PaypalConstants.PAYPAL_CLIENT_ID);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(total)),
                "USD", "payment", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent paymentIntent = new Intent(getActivity(), PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(paymentIntent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString();
                        Log.i("paypal","payment is received");
                        Log.i("Payment Details: ", paymentDetails);
                        placeOrderFromCart();
                        //      Fragment fragment = new CartFragment();
                        //      getActivity().getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                    }catch (Exception e){
                        Log.i("paypal","error getting payment");
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void placeOrderFromCart() {
        for(int i = 0; i < MainActivity.cartItems.size(); i++){
            cartItem = MainActivity.cartItems.get(i);
            placeOrderForSingleItem(cartItem);
        }
    }


    private void placeOrderForSingleItem(CartItem cartItem){
        Map<String, String> order = new HashMap<>();
        String userString = sp.getString("user", null);
        userInfo = gson.fromJson(userString, UserInfo.class);
Log.d("CartFragment: ", userInfo.getUserID());
        order.put("item_id", cartItem.getProductItem().getId());
        order.put("item_names", cartItem.getProductItem().getProductName());
        order.put("final_price", cartItem.getProductItem().getPrize());
        order.put("item_quantity", cartItem.getQuantity());
        order.put("mobile", userInfo.getUserMobile());
        order.put("api_key", userInfo.getAppApiKey());
        order.put("user_id", userInfo.getUserID());

//final String name = cartItem.getProductItem().getProductName();

        Call<String> call = apiInterface.makeOrder(order);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().contains("Order Confirmed")) {
                    MainActivity.cartItems.clear();
                    adapter.notifyDataSetChanged();
                    if (MainActivity.cartItems.size() == 0) tvHint.setVisibility(View.VISIBLE);
Log.d("retrofit placing order", "success");
                } else {
Log.d("retrofit placing order", "error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
Log.d("placeOrder", t.toString());
            }
        });
    }

    private static class PaypalConstants{
        static final String PAYPAL_CLIENT_ID =
                "AQ5WgA6nIeos6yOaCSvGQ06SdGsGYc2djUpkUVMLCm4QdCpMN2rGqEAACnzZ6pkNQCyVf4RSrStK3FG_";
    }
}
