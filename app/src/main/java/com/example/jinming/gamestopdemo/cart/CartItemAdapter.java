package com.example.jinming.gamestopdemo.cart;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jinming.gamestopdemo.MainActivity;
import com.example.jinming.gamestopdemo.R;
import com.example.jinming.gamestopdemo.product.ProductItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jinming on 11/29/17.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> cartItems;

    public CartItemAdapter(Context context){
        this.context = context;
        cartItems = MainActivity.cartItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductItem item = cartItems.get(position).getProductItem();
        holder.tvQuan.setText(cartItems.get(position).getQuantity());
        holder.tvPrice.setText(item.getPrize());
        holder.tvName.setText(item.getProductName());
        holder.tvDesc.setText(item.getDiscription());
        Picasso.with(context).load(item.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView cardView;
        private ImageView imageView;
        private TextView tvName, tvDesc, tvQuan, tvPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivCart);
            tvDesc = itemView.findViewById(R.id.tvDescription);
            tvName = itemView.findViewById(R.id.tvNameCart);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuan = itemView.findViewById(R.id.tvQuan);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
