package com.example.parulsingh.represent2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VoteFragment extends CardFragment {

    @Override
    public View onCreateContentView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.vote_fragment_layout, container, false);
        TextView countyText = (TextView) view.findViewById(R.id.county);
        countyText.setText(getArguments().getString("data"));
        countyText.setTextColor(Color.parseColor("#000000"));
        return view;
    }


    public static CardFragment create(String data) {
        CardFragment fragment = new VoteFragment();
        Bundle args = new Bundle();
        args.putCharSequence("data", data);
        fragment.setArguments(args);
        return fragment;
    }

}