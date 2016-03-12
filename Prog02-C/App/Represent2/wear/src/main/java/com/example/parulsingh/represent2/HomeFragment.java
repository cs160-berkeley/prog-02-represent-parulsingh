package com.example.parulsingh.represent2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends CardFragment {

    @Override
    public View onCreateContentView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setTextColor(Color.parseColor("#000000"));
        return view;
    }

    public static CardFragment create() {
        CardFragment fragment = new HomeFragment();
        return fragment;
    }

}