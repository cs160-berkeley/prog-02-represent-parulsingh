package com.example.parulsingh.represent2;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        if( messageEvent.getPath().equals("/json")) {
            String text = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            String json = text.split("voteSep")[0];
            String vote = text.split("voteSep")[1];
            Intent intent = new Intent(this, MainActivity.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.putExtra("JSON", json);
            mainIntent.putExtra("VOTE", vote);
            Log.d("T", "in WatchListenerService, got: " + "here");
            startActivity(mainIntent);
        }
    }
}

