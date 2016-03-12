package com.example.parulsingh.represent2;

import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().hasExtra("LATLOG")){
            String latlog = getIntent().getStringExtra("LATLOG");
            latlog = latlog.substring(0,latlog.length()-1);
            String lat = latlog.split(",")[0];
            String log = latlog.split(",")[1];
            handleShake(lat, log);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleApiClient.connect();

            }
        });

        final EditText zipcode = (EditText) findViewById(R.id.editText);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countyInfo = "";
                String voteData = "";
                try {
                    countyInfo = new MyTask().execute("https://maps.googleapis.com/maps/api/geocode/json?address=" + zipcode.getText().toString() + "&key=AIzaSyDbsO3xnrVgyUwLZcX2YknBC_AlosiAAFo").get();
                    Log.d("T", "county info " + countyInfo);
                    JSONArray resultsArray = new JSONObject(countyInfo).getJSONArray("results");
                    JSONObject obj = (JSONObject) resultsArray.get(0);
                    JSONObject location = obj.getJSONObject("geometry").getJSONObject("location");
                    String lat = location.getString("lat");
                    String log = location.getString("lng");

                    String countyInfo2 = new MyTask().execute("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + log + "&key=AIzaSyDbsO3xnrVgyUwLZcX2YknBC_AlosiAAFo").get();
                    JSONArray resultsArray2 = new JSONObject(countyInfo2).getJSONArray("results");
                    JSONObject obj2 = (JSONObject) resultsArray2.get(0);
                    JSONArray components2 = obj2.getJSONArray("address_components");
                    Log.d("T", "countyinfo 2" + countyInfo2);
                    int level2 = findIndexOfLevel(components2);
                    Log.d("T", "level2" + level2);
                    String county = getCounty(components2, level2);
                    String state = ((JSONObject) components2.get(level2 + 1)).getString("short_name");
                    voteData = getVoteData(county, state);
                    Log.d("T", "county" + county);
                    Log.d("T", "state" + state);
                    voteData = getVoteData(county, state);
                } catch (Exception e) {
                    Log.d("T", "error" + Log.getStackTraceString(e));
                }

                String loc = "zip=" + zipcode.getText().toString();
                String jsonZipcode = "";
                try {
                    jsonZipcode = new MyTask()
                            .execute("http://congress.api.sunlightfoundation.com/legislators/locate?" + loc + "&apikey=0c2ae6d857284991b96fcf21c801eb41").get();
                } catch (Exception e) {
                }
                if (jsonZipcode.equals("{\"results\":[],\"count\":0}")) {
                    Toast.makeText(getApplicationContext(), "No results for this zipcode", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(MainActivity.this, CongressionalActivity.class);
                    myIntent.putExtra("JSON", jsonZipcode);
                    MainActivity.this.startActivity(myIntent);
                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent.putExtra("JSON", jsonZipcode);
                    sendIntent.putExtra("VOTE", voteData);
                    startService(sendIntent);
                }
            }
        });
    }
    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            String lat = String.valueOf(mLastLocation.getLatitude());
            String log = String.valueOf(mLastLocation.getLongitude());
            handleShake(lat, log);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    void handleShake(String lat, String log){
        String countyInfo = "";
        String voteData = "";
        try {
            countyInfo = new MyTask().execute("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat + "," + log + "&key=AIzaSyDbsO3xnrVgyUwLZcX2YknBC_AlosiAAFo").get();
            JSONArray resultsArray = new JSONObject(countyInfo).getJSONArray("results");
            JSONObject obj = (JSONObject)resultsArray.get(0);
            JSONArray components = obj.getJSONArray("address_components");
            int level2 = findIndexOfLevel(components);
            String county = getCounty(components, level2);
            String state = ((JSONObject)components.get(level2+1)).getString("short_name");
            voteData = getVoteData(county, state);
        } catch (Exception e) {}

        String loc = "latitude=" + lat +"&longitude=" + log;
        String jsonLocation = "";
        try {
            jsonLocation = new MyTask().execute("http://congress.api.sunlightfoundation.com/legislators/locate?" + loc + "&apikey=0c2ae6d857284991b96fcf21c801eb41").get();
        } catch (Exception e) {}
        if (jsonLocation.equals("{\"results\":[],\"count\":0}")) {
            Toast.makeText(getApplicationContext(), "No results for this location", Toast.LENGTH_SHORT).show();
        } else {
            Intent myIntent = new Intent(MainActivity.this, CongressionalActivity.class);
            myIntent.putExtra("JSON", jsonLocation);
            MainActivity.this.startActivity(myIntent);

            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            sendIntent.putExtra("JSON", jsonLocation);
            sendIntent.putExtra("VOTE", voteData);
            startService(sendIntent);
        }
    }

    private class MyTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params){
            String url = params[0];
            try {
                return new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();

            } catch (Exception e){}
            return null;
        }

    }

    String getVoteData(String county, String state) throws IOException{
        AssetManager assetManager = getResources().getAssets();
        String jsonString = "";
        try {
            InputStream stream = assetManager.open("election-county-2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            jsonString = new String(buffer, "UTF-8");

        } catch (IOException e) {}
        try {
            JSONArray voteInfo = new JSONArray(jsonString);

            for (int i = 0; i < voteInfo.length(); i++) {
                JSONObject j = (JSONObject) voteInfo.get(i);
                if (j.getString("county-name").equals(county) && j.getString("state-postal").equals(state)) {
                    return county + " County, " + state + "\n" + j.get("obama-percentage") + "% Obama\n" + j.get("romney-percentage") + "% Romney";
                }
            }
        } catch (JSONException e) {
            Log.d("T", "JSONe" + e.getMessage());

        }
        return county + " County, " + state + "\nUnknown 2012 Vote Data";
    }

    String getCounty(JSONArray components, int index){
        try {
            String county = ((JSONObject) components.get(index)).getString("short_name");
            String[] countyWords = county.split(" ");
            if (countyWords[countyWords.length - 1].equals("County")) {
                countyWords[countyWords.length - 1] = "";
                county = "";
                for (String elem : countyWords) {
                    county += elem + " ";
                }
                county = county.trim();
            }
            return county;
        } catch (JSONException e){}
        return null;
    }

   int findIndexOfLevel(JSONArray components) throws JSONException {
       for (int i = 0; i < components.length(); i++){
           JSONObject j = components.getJSONObject(i);
           JSONArray types = (JSONArray)j.get("types");
           if (types.getString(0).equals("administrative_area_level_2")) {
               return i;
           }
       }
       return 0;
   }

}
