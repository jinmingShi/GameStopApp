package com.example.jinming.gamestopdemo.product;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jinming.gamestopdemo.R;
import com.example.jinming.gamestopdemo.network.ApiClient;
import com.example.jinming.gamestopdemo.network.ApiInterface;


import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jinming on 11/27/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<CategoryItem> categoryItemList;
    private List<SubCategoryItem> subCategoryItemList;
    List<ProductItem> productItemList;

    private LayoutInflater inflater;
    private RecyclerViewClickListener mListener;
//    ApiInterface apiInterface;
//    SharedPreferences sp;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    public ProductAdapter(Context context, List<CategoryItem> categoryItemList,
                          List<SubCategoryItem> subCategoryItemList,
                          List<ProductItem> productItemList) {
        this.inflater = LayoutInflater.from(context);
        if (categoryItemList != null) {
            this.categoryItemList = categoryItemList;
        } else if (subCategoryItemList != null) {
            this.subCategoryItemList = subCategoryItemList;
        } else {
            this.productItemList = productItemList;
        }
        //sp = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        //apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (categoryItemList != null) {
            CategoryItem item = categoryItemList.get(position);
            //Log.d("ITEM: ", item.toString());
            viewHolder.categoryName.setText(item.getCatagoryName());
            viewHolder.categoryDiscription.setText(item.getCatagoryDiscription());
            new DownloadImageTask(viewHolder.categoryImage).execute(item.getCatagoryImage());
        } else if (subCategoryItemList != null) {
            SubCategoryItem item = subCategoryItemList.get(position);
            viewHolder.categoryName.setText(item.getSubCatagoryName());
            viewHolder.categoryDiscription.setText(item.getSubCatagoryDiscription());
            new DownloadImageTask(viewHolder.categoryImage).execute(item.getCatagoryImage());
        } else {
            ProductItem productItem = productItemList.get(position);
            viewHolder.categoryName.setText(productItem.getProductName());
            viewHolder.categoryDiscription.setText(productItem.getDiscription());
            new DownloadImageTask(viewHolder.categoryImage).execute(productItem.getImage());
        }
        if (mListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(viewHolder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (categoryItemList != null) {
            return categoryItemList.size();
        } else if (subCategoryItemList != null) {
            return subCategoryItemList.size();
        } else {
            return productItemList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView id;
        TextView categoryName;
        TextView categoryDiscription;
        private RecyclerViewClickListener mListener;

        public ViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            this.mListener = listener;
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryDiscription = (TextView) view.findViewById(R.id.categoryDescription);
            categoryImage = (ImageView) view.findViewById(R.id.image);
        }
    }

    public void setOnItemClickLitener(RecyclerViewClickListener mOnItemClickListener) {
        this.mListener = mOnItemClickListener;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Load_Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
