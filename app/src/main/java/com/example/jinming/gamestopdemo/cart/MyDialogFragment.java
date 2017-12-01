package com.example.jinming.gamestopdemo.cart;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.MainActivity;
import com.example.jinming.gamestopdemo.R;
import com.example.jinming.gamestopdemo.product.ProductItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private ProductItem productItem;
    private ImageView imageView;
    private TextView tvAsk, tvName, tvQuan;
    private Button btnCancel, btnYes;
    private Spinner spinner;

    public MyDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MyDialogFragment(ProductItem currentItem) {
        this.productItem = currentItem;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container,false);
        imageView = view.findViewById(R.id.ivCart);
        tvName = view.findViewById(R.id.tvNameCart);
        spinner = view.findViewById(R.id.spinner);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnYes = view.findViewById(R.id.btnYes);
        addSpinner(productItem.getQuantity());
        btnCancel.setOnClickListener(this);
        btnYes.setOnClickListener(this);
        Picasso.with(getActivity()).load(productItem.getImage()).into(imageView);
        tvName.setText(productItem.getProductName());
        return view;
    }

    private void addSpinner(String quantity) {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<=Integer.valueOf(quantity); i++){
            list.add(""+i);
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnYes:
                String quantity = spinner.getSelectedItem().toString();
                if(quantity.equals("0")){
                    Toast.makeText(getActivity(), "Please select a quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                //add to cart
                CartItem cartItem = new CartItem(productItem, quantity);
                MainActivity.cartItems.add(cartItem);
                Toast.makeText(getActivity(), quantity + " " + productItem.getProductName() + " added!", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }
}
