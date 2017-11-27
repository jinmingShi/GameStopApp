package com.example.jinming.gamestopdemo;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SlidePageFragment extends Fragment {
    Button btn;
    ImageView imageView;
    private int index;

    public SlidePageFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public SlidePageFragment(int index) {
        this.index = index;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide_page, container, false);
        imageView = view.findViewById(R.id.imageView);
        btn = view.findViewById(R.id.button);

        switch (index) {
            case 0:
                imageView.setImageResource(R.drawable.first);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case 1:
                imageView.setImageResource(R.drawable.second);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setBackgroundColor(Color.parseColor("#181818"));
                btn.setText(R.string.btn_find);
                break;
            case 2:
                imageView.setImageResource(R.drawable.three);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setBackgroundColor(Color.parseColor("#222222"));
                break;
            case 3:
                imageView.setImageResource(R.drawable.four);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case 4:
                imageView.setImageResource(R.drawable.five);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setBackgroundColor(Color.parseColor("#373737"));
                break;
            case 5:
                imageView.setImageResource(R.drawable.six);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setBackgroundColor(Color.parseColor("#373737"));
                break;
            case 6:
                imageView.setImageResource(R.drawable.seven);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
        return view;
    }

}
