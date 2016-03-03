package com.example.parulsingh.represent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wearable.view.BoxInsetLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenSlidePageFragment3 extends Fragment {
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page3, container, false);
        Button moreInfo = (Button) rootView.findViewById(R.id.moreInfo);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
                sendIntent.putExtra("NAME", "honda");
                getActivity().startService(sendIntent);
            }
        });
        if (getActivity().getIntent().hasExtra("ZIPCODE") && getActivity().getIntent().getStringExtra("ZIPCODE").equals("95070")){
            updateContent2();
        }
        return rootView;
    }
    public void updateContent() {
        BoxInsetLayout b = (BoxInsetLayout)rootView.findViewById(R.id.content);
        b.setBackgroundColor(Color.parseColor("#EB5757"));
        TextView text = (TextView)rootView.findViewById(R.id.text);
        text.setText("Senator Richard Shelby\nRepublican");
        ImageView img = (ImageView)rootView.findViewById(R.id.pic);
        if (isAdded()) {
            Drawable myIcon = getResources().getDrawable(R.mipmap.shelby);
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