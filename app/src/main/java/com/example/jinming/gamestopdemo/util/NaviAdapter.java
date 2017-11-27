package com.example.jinming.gamestopdemo.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinming.gamestopdemo.R;

import java.util.List;

/**
 * Created by Jinming on 11/24/17.
 */

public class NaviAdapter extends ArrayAdapter<Item> {
    private Activity activity;
    private List<Item> list;
    private TextView textView;
    private ImageView imageView;

    public NaviAdapter(Activity activity, Context context, List<Item> list) {
        super(context, 0, list);
        this.list = list;
        this.activity = activity;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Item item = list.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }
        textView = convertView.findViewById(R.id.textView);
        imageView = convertView.findViewById(R.id.imageView);
        imageView.setImageResource(item.getImageView());
        textView.setText(item.getName());
        //textView.setCompoundDrawablesWithIntrinsicBounds(item.getImageView(), 0, 0, 0);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }
}
