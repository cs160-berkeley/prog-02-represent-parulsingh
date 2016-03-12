package com.example.parulsingh.represent2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CongressionalFragment extends CardFragment implements View.OnClickListener {
    static String party;

    @Override
    public View onCreateContentView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        Button moreInfo = (Button) view.findViewById(R.id.button);
        TextView nameText = (TextView) view.findViewById(R.id.name);
        TextView partyText = (TextView) view.findViewById(R.id.party);
        //ImageView image = (ImageView) view.findViewById(R.id.img);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.linLayout);

        moreInfo.setOnClickListener(this);
        nameText.setText(getArguments().getString("name"));
        nameText.setTextColor(Color.parseColor("#000000"));

        party = getArguments().getString("party");
        partyText.setText(getArguments().getString("party"));
        partyText.setTextColor(Color.parseColor("#000000"));
        //image.setImageDrawable(getActivity().getDrawable(p.photo));
        //box = (BoxInsetLayout) view.findViewById(R.id.box);
        //box.setBackground(getActivity().getDrawable(R.mipmap.barb));
        return view;
    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        Intent sendIntent = new Intent(getActivity().getBaseContext(), WatchToPhoneService.class);
        sendIntent.putExtra("BIOGUIDE", getArguments().getString("bioguide"));
        getActivity().startService(sendIntent);
    }

    public static CardFragment create(String name, String party, String bioguide) {
        CardFragment fragment = new CongressionalFragment();
        Bundle args = new Bundle();
        args.putCharSequence("name", name);
        args.putCharSequence("party", party);
        args.putCharSequence("bioguide", bioguide);
        fragment.setArguments(args);
        return fragment;
    }


}