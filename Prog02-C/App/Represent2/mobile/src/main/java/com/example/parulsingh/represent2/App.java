package com.example.parulsingh.represent2;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by parulsingh on 3/7/16.
 */
public class App extends Application {
    private static final String CONSUMER_KEY = "ir2LOt5yhHB0pO8aWoQCHaLJB";
    private static final String CONSUMER_SECRET = "MW25tYNKBuIDd8FqKOOGPF3vceqCHo9OH2ycwhRzL4tFZQHz3Z";
    private TwitterAuthConfig authConfig;
    private static App singleton;

    public static App getInstance() {
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        authConfig
                = new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
