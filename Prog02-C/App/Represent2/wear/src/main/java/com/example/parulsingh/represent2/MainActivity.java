package com.example.parulsingh.represent2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends FragmentActivity {
    static String json;
    static String vote;
    static JSONObject person1 = new JSONObject();
    static JSONObject person2 = new JSONObject();
    static JSONObject person3 = new JSONObject();
    static JSONObject person4 = null;
    static Boolean isObama = false;
    static boolean has2016data = false;
    static String currentVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        person4 = null;
        Log.d("T", "in WatchListenerService, got: " + "here2");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        has2016data = false;
        currentVote = null;
        json = getIntent().getStringExtra("JSON");
        vote = getIntent().getStringExtra("VOTE");
        if (vote != null && !vote.contains("Unknown 2012 Vote Data")) {
            currentVoteData();
            String[] data = vote.split("\\r?\\n");
            String obama = data[1].split(" ")[0];
            double ob = Double.parseDouble(obama.substring(0, obama.length() - 1));
            String romney = data[2].split(" ")[0];
            double ro = Double.parseDouble(romney.substring(0, romney.length() - 1));
            Log.d("T", "in main, ob: " + ob);
            Log.d("T", "in main, ro: " + ro);
            if (ob > ro) {
                isObama = true;
            } else if (ro > ob) {
                isObama = false;
            } else {
                isObama = null;
            }
        }
        Log.d("T", "in main, got: " + vote);
        final DotsPageIndicator mPageIndicator;
        final GridViewPager mViewPager;

        // Get UI references
        mPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        mViewPager = (GridViewPager) findViewById(R.id.pager);

        // Assigns an adapter to provide the content for this pager
        mViewPager.setAdapter(new GridPagerAdapter(getFragmentManager(), json, this));
        mPageIndicator.setPager(mViewPager);

        mViewPager.setOnPageChangeListener(new GridViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int row, int column, float rowOffset,
                                       float columnOffset, int rowOffsetPixels,
                                       int columnOffsetPixels) {
                // This is called when the scroll state changes
                mPageIndicator.onPageScrolled(row, column, rowOffset,
                        columnOffset, rowOffsetPixels, columnOffsetPixels);
            }

            @Override
            public void onPageSelected(int row, int column) {
                BitmapDrawable obama = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.obama_web));
                BitmapDrawable romney = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.romney_web));
                try {
                    if (column == 0)
                        findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#4A125E"));
                    else if (column == 1) {
                        doOnFragSelected(person1);
                    } else if (column == 2) {
                        doOnFragSelected(person2);
                    } else if (column == 3) {
                        doOnFragSelected(person3);
                    } else if (column == 4) {
                        if (person4 != null) {
                            doOnFragSelected(person4);
                        } else {
                            findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#4A125E"));                        }
                    } else if (column == 5) {
                        findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#4A125E"));
                    } else if (column == 6 && has2016data){
                        findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#4A125E"));
                    }
                    mPageIndicator.onPageSelected(row, column);
                } catch (Exception e) {}
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mPageIndicator.onPageScrollStateChanged(state);
            }
        });


    }

    private static final class GridPagerAdapter extends FragmentGridPagerAdapter {

        private JSONArray arr = new JSONArray();
        private final Context mContext;


        private GridPagerAdapter(FragmentManager fm, String json, Context ctx) {
            super(fm);
            mContext = ctx;
            if (json != null) {
                try {
                    arr = new JSONObject(json).getJSONArray("results");
                    person1 = arr.getJSONObject(0);
                    person2 = arr.getJSONObject(1);
                    person3 = arr.getJSONObject(2);
                    if (arr.length() == 4)
                        person4 = arr.getJSONObject(3);
                    Log.d("T", "in perosn, got: " + person4);
                } catch (JSONException e) {
                }
            }
        }

        @Override
        public Fragment getFragment(int row, int column) {
            Log.d("T", "Calling getFragment with: " + column);
            Fragment f = new Fragment();
            if (column == 0)
                f = HomeFragment.create();
            if (column == 1 && json != null)
                f = createPersonFragment(person1);
            else if (column == 2 && json != null)
                f = createPersonFragment(person2);
            else if (column == 3 && json != null)
                f = createPersonFragment(person3);
            else if (column == 4 && json != null) {
                if (person4 != null)
                    f = createPersonFragment(person4);
                else
                    f = VoteFragment.create(vote);
            } else if (column == 5) {
                if (person4 != null)
                    f = createVoteFragment();
                else if (has2016data)
                    f = create2016VoteFragment();
            } else if (column == 6 && has2016data) {
                f = create2016VoteFragment();
            }
            return f;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int row) {
            if (has2016data)
                return arr.length()+3;
            else
                return arr.length()+2;
        }
        Fragment createVoteFragment() {
            return VoteFragment.create(vote);
        }

        Fragment create2016VoteFragment() {
            return CurrentVoteFragment.create(currentVote);
        }

        Fragment createPersonFragment(JSONObject person) {
            String party = "";
            String title = "";
            String name = "";

            try {
                if (person.getString("party").equals("D")) {
                    party = "Democrat";
                } else if (person.getString("party").equals("R")) {
                    party = "Republican";
                } else if (person.getString("party").equals("I")) {
                    party = "Independent";
                }
                if (person.getString("title").equals("Rep")) {
                    title = "Representative";
                } else if (person.getString("title").equals("Sen")) {
                    title = "Senator";
                }
                name = title + "\n" + person.getString("first_name") + " " + person.getString("last_name");
                Log.d("T", "in MainWear: " + name);
                return CongressionalFragment.create(name, party, person.getString("bioguide_id"));
            } catch (JSONException e ) {}
            return null;
        }

/*
        @Override
        public Drawable getBackgroundForPage(int row, int col) {
            return mContext.getDrawable(mData[row][col].photo);
        }
*/
    }

    private SensorManager mSensorManager;
    private float prevmAccel = 0;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    int count = 0;
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            String loc = randomLocation();
            //Log.d("T", "in pamyp, got: " + loc);
            Context context2 = getApplicationContext();
            //Toast toast2 = Toast.makeText(context2, loc, Toast.LENGTH_SHORT);
            //toast2.show();
            prevmAccel = mAccel;
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 50) {
                Context context = getApplicationContext();
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, Float.toString(mAccel), duration);
//                toast.show();
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.putExtra("SHAKE", loc);
                startService(sendIntent);
                MainActivity.this.finish();

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    String randomLocation() {
        AssetManager assetManager = getResources().getAssets();
        ArrayList<String> locs = new ArrayList<String>();
        String line = "";
        try {
            InputStream stream = assetManager.open("us_postal_codes.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
            while ((line = br.readLine()) != null) {
                locs.add(line);
            }

        } catch (IOException e) {
            Log.d("T", "in pamp, got: " + Log.getStackTraceString(e));

        }
        if (locs.size() > 0) {
            Random rand = new Random();
            int randNum = rand.nextInt(locs.size());
            //int randNum = (int) Math.random() * locs.size();
            return locs.get(randNum);
        }
        return "wrong";
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
    }

    void doOnFragSelected(JSONObject person) throws Exception {
//        Bitmap bitmap = new DownloadImageTask()
//                .execute("https://theunitedstates.io/images/congress/450x550/" + person.getString("bioguide_id") + ".jpg").get();
//        if (bitmap == null) {
        if (person.getString("party").equals("D"))
            findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#2D9CDB"));
        else
            findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#EB5757"));
//        } else {
//            Drawable d = new BitmapDrawable(getResources(), bitmap);
//            findViewById(R.id.framelayout).setBackground(d);
//        }
    }
    void setVoteBackground(Drawable obama, Drawable romney) {
        //CircularImageView img = (CircularImageView)findViewById(R.id.backimg);
        if (isObama != null && isObama)
            findViewById(R.id.framelayout).setBackground(obama);
        else if (isObama != null && !isObama)
            findViewById(R.id.framelayout).setBackground(romney);
        else
            findViewById(R.id.framelayout).setBackgroundColor(Color.parseColor("#4A125E"));
    }

    void currentVoteData() {
        if (!vote.contains("Unknown 2012 Vote Data")) {
            String county = (vote.split("County,")[0]).trim();
            Log.d("T", "in pamp2, county: " + county);
            String state = (vote.split("County,")[1]).split("\n")[0].trim();
            Log.d("T", "in pamp2, got: state " + state);
            AssetManager assetManager = getResources().getAssets();
            ArrayList<String> data = new ArrayList<String>();
            String line = "";
            try {
                InputStream stream = assetManager.open("results.csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }

            } catch (IOException e) {
                Log.d("T", "in pamp2, got: " + Log.getStackTraceString(e));

            }
            String result = "";
            for (int i = 0; i < data.size(); i++) {
                String[] splitLine = data.get(i).split(",");
                if (splitLine[0].equals(state) && splitLine[1].equals(county)) {
                    result += splitLine[2] + " " + splitLine[4] + "%\n";
                }
            }
            if (!result.equals("")) {
                has2016data = true;
                Log.d("T", "in pamp2, got result: " + result);
                currentVote = county + " County, " + state + "\n" + result;
            }
        }
    }
}
