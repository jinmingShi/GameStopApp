package com.example.jinming.gamestopdemo;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jinming.gamestopdemo.util.Fragment3Adapter;
import com.example.jinming.gamestopdemo.util.Sentence;
import com.example.jinming.gamestopdemo.util.UtilArray;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment3 extends Fragment {
    List<Sentence> list = Sentence.get();
    ListView listView;
    Fragment3Adapter adapter;

    public TabFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);
        listView = view.findViewById(R.id.listView);
        adapter = new Fragment3Adapter(getContext(), 0, list);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
