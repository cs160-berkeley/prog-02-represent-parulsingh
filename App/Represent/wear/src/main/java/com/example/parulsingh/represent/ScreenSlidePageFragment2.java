package com.example.parulsingh.represent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wearable.view.BoxInsetLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parulsingh.represent.R;

public class ScreenSlidePageFragment2 extends Fragment {
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page2, container, false);
        if (getActivity().getIntent().hasExtra("ZIPCODE") && getActivity().getIntent().getStringExtra("ZIPCODE").equals("95070")){
            updateContent2();
        }
        return rootView;
    }
    public void updateContent() {
        BoxInsetLayout b = (BoxInsetLayout)rootView.findViewById(R.id.content);
        b.setBackgroundColor(Color.parseColor("#EB5757"));
        TextView text = (TextView)rootView.findViewById(R.id.text);
        text.setText("Senator Jefferson Sessions\nRepublican");
        ImageView img = (ImageView)rootView.findViewById(R.id.pic);
        if (isAdded()) {
            Drawable myIcon = getResources().getDrawable(R.mipmap.shel);
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