package com.example.parulsingh.represent2;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DismissOverlayView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VoteActivity extends WearableActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private TextView mTextView;
    private MapFragment mMapFragment;
    private static final LatLng SYDNEY = new LatLng(-33.85704, 151.21522);
    private GoogleMap mMap;
    private DismissOverlayView mDismissOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        mDismissOverlay =
                (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText("Long press to exit map");
        mDismissOverlay.showIntroIfNecessary();

        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    public void onMapLongClick(LatLng point) {
        mDismissOverlay.show();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.addMarker(new MarkerOptions().position(SYDNEY)
                .title("Sydney Opera House"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 10));
        mMap.setOnMapLongClickListener(this);
    }

}
