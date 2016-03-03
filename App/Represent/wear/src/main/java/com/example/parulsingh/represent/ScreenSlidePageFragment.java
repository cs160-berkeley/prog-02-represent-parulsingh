package com.example.parulsingh.represent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        if (getActivity().getIntent().hasExtra("ZIPCODE") && getActivity().getIntent().getStringExtra("ZIPCODE").equals("95070")){
           updateContent2();
        }
        return rootView;
    }

    public void updateContent() {
        BoxInsetLayout b = (BoxInsetLayout)rootView.findViewById(R.id.content);
        b.setBackgroundColor(Color.parseColor("#EB5757"));
        TextView text = (TextView)rootView.findViewById(R.id.text);
        text.setText("Congressman Mike Rodgers\nRepublican");
        ImageView img = (ImageView)rootView.findViewById(R.id.pic);
        if (isAdded()) {
            Drawable myIcon = getResources().getDrawable(R.mipmap.mikerodgers);
            img.setImageDrawable(myIcon);
        }
    }
    public void updateContent2() {
        BoxInsetLayout b = (BoxInsetLayout) rootView.findViewById(R.id.content);
        b.setBackgroundColor(Color.parseColor("#2D9CDB"));
        TextView text = (TextView) rootView.findViewById(R.id.text);
        text.setText("Senator Dianne Feinstein\nDemocrat");
        ImageView img = (ImageView) rootView.findViewById(R.id.pic);
        if (isAdded()) {
            Drawable myIcon = getResources().getDrawable(R.mipmap.eshoo);
            img.setImageDrawable(myIcon);
        }
    }
}