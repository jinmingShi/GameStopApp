package com.example.jinming.gamestopdemo.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jinming.gamestopdemo.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Jinming on 11/25/17.
 */

public class Fragment3Adapter extends ArrayAdapter<Sentence> {
    List<Sentence> list;
    TextView tv1, tv2;

    public Fragment3Adapter(@NonNull Context context, int resource, List<Sentence> list) {
        super(context, resource, list);
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Sentence s = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment3_list_item, parent, false);
        }
        tv1 = convertView.findViewById(R.id.textView1);
        tv2 = convertView.findViewById(R.id.textView2);

        tv1.setText(s.getTitle());
        tv2.setText(s.getContent());
        if (position == 0) {
            tv1.setTextColor(Color.WHITE);
            tv1.setTextSize(27);
            tv2.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(Color.parseColor("#e74c3c"));
        }
        return convertView;
    }
}
