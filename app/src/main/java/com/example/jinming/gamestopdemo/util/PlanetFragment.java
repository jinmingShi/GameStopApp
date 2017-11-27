package com.example.jinming.gamestopdemo.util;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinming.gamestopdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanetFragment extends Fragment {


    public PlanetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planet, container, false);
    }


}
