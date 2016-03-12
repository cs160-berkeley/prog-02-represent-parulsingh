package com.example.parulsingh.represent2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CongressionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_congressional);
            JSONObject obj = new JSONObject(getIntent().getStringExtra("JSON"));
            JSONArray arr = obj.getJSONArray("results");

            final JSONObject person1 = arr.getJSONObject(0);
            final JSONObject person2 = arr.getJSONObject(1);
            final JSONObject person3 = arr.getJSONObject(2);
            final JSONObject person4;

            final TextView text1 = (TextView) findViewById(R.id.info_text);
            final CircularImageView image1 = (CircularImageView) findViewById(R.id.image1);
            final ImageButton tweet1 = (ImageButton) findViewById(R.id.latestTweet1);
            Button button1 = (Button) findViewById(R.id.button1);
            populateFields(person1, text1, image1, button1, tweet1);

            final TextView text2 = (TextView) findViewById(R.id.info_text2);
            final CircularImageView image2 = (CircularImageView) findViewById(R.id.image2);
            final ImageButton tweet2 = (ImageButton) findViewById(R.id.latestTweet2);
            Button button2 = (Button) findViewById(R.id.button2);
            populateFields(person2, text2, image2, button2, tweet2);


            final TextView text3 = (TextView) findViewById(R.id.info_text3);
            final CircularImageView image3 = (CircularImageView) findViewById(R.id.image3);
            final ImageButton tweet3 = (ImageButton) findViewById(R.id.latestTweet3);
            Button button3 = (Button) findViewById(R.id.button3);
            populateFields(person3, text3, image3, button3, tweet3);

            if (arr.length() == 4) {
                person4 = arr.getJSONObject(3);
                final CardView card4 = (CardView) findViewById(R.id.card_view4);
                card4.setVisibility(View.VISIBLE);

                final TextView text4 = (TextView) findViewById(R.id.info_text4);
                final CircularImageView image4 = (CircularImageView) findViewById(R.id.image4);
                final ImageButton tweet4 = (ImageButton) findViewById(R.id.latestTweet4);
                Button button4 = (Button) findViewById(R.id.button4);
                populateFields(person4, text4, image4, button4, tweet4);
            }
        } catch(JSONException e) {
            e.printStackTrace();
            Log.d("T", "in Congress, got error1: " + e.getMessage());
        }
    }

    void populateFields(final JSONObject person, TextView text, CircularImageView img, Button button, ImageButton tweetButton) throws JSONException {
        String party = "";
        if (person.getString("party").equals("D")){
            img.setBorderColor(Color.parseColor("#2D9CDB"));
            party = "Democrat";
        } else if (person.getString("party").equals("R")){
            party = "Republican";
            img.setBorderColor(Color.parseColor("#EB5757"));
        } else if (person.getString("party").equals("I")) {
            party = "Independent";
        }
        String title = "";
        if (person.getString("title").equals("Rep")){
            title = "Representative";
        } else if (person.getString("title").equals("Sen")){
            title = "Senator";
        }
        text.setText(title + " " + person.getString("first_name") + " " + person.getString("last_name") + "\n" + party + "\n" + person.getString("website") + "\n" + person.getString("oc_email"));
        new DownloadImageTask((ImageView) img)
                .execute("https://theunitedstates.io/images/congress/450x550/" + person.getString("bioguide_id") + ".jpg");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                try {
                    Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
                    intent.putExtra("PERSON", person.getString("bioguide_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    Log.d("T", "in Congress, JSON: " + e.getMessage());

                }
            }
        });
        tweetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                try {
                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                    StatusesService statusesService = twitterApiClient.getStatusesService();
                    statusesService.userTimeline(null, person.getString("twitter_id"), null, (long)1, null, null, null, null, null, new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                            //Do something with result, which provides a Tweet inside of result.data
                            final TweetView tweetView = new TweetView(CongressionalActivity.this, result.data.get(0), R.style.tw__TweetDarkStyle);
                            tweetView.setGravity(Gravity.BOTTOM);
                            final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
                            parentView.addView(tweetView);
                            tweetView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tweetView.setVisibility(View.GONE);
                                }
                            }, 5000);
                        }

                        public void failure(TwitterException exception) {
                            //Do something on failure
                        }
                    });
                } catch (JSONException e){}

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}



