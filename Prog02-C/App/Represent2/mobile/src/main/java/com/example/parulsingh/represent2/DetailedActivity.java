package com.example.parulsingh.represent2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class DetailedActivity extends AppCompatActivity {
    static String out;
    static String out2;
    static String out3;

    @Override
    protected void onPause() {
        super.onPause();
        out = null;
        out2 = null;
        out3 = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        String bioguide = "";
        if (getIntent().hasExtra("PERSON"))
        {
            bioguide = getIntent().getStringExtra("PERSON");
            Log.d("T", "in Detailed, got : " + bioguide);

        }
        try {
            String output = "";
            new MyTask(output)
                    .execute("http://congress.api.sunlightfoundation.com/legislators?bioguide_id=" + bioguide + "&apikey=0c2ae6d857284991b96fcf21c801eb41\n");
            while (out == null) {}
            JSONObject person  = new JSONObject(out).getJSONArray("results").getJSONObject(0);;

            String output2 = "";
            new MyTask(output2)
                    .execute("http://congress.api.sunlightfoundation.com/committees?subcommittee=false&member_ids="+bioguide+"&apikey=0c2ae6d857284991b96fcf21c801eb41");
            while (out2 == null) {}
            JSONArray coms = new JSONObject(out2).getJSONArray("results");

            String output3 = "";
            new MyTask(output3)
                    .execute("http://congress.api.sunlightfoundation.com/bills?sponsor_id="+bioguide+"&apikey=0c2ae6d857284991b96fcf21c801eb41\n");
            while (out3 == null) {}
            JSONArray billsArray = new JSONObject(out3).getJSONArray("results");

            CircularImageView img = (CircularImageView) findViewById(R.id.img);
            new DownloadImageTask(img)
                    .execute("https://theunitedstates.io/images/congress/450x550/" + person.getString("bioguide_id") + ".jpg");

            String party = person.getString("party");
            String title = person.getString("title");
            View l = findViewById(R.id.line);
            TextView text1 = (TextView)findViewById(R.id.text1);
            TextView text2 = (TextView)findViewById(R.id.text2);
            TextView text3 = (TextView)findViewById(R.id.text3);

            if (person.getString("party").equals("D")){
                img.setBorderColor(Color.parseColor("#2D9CDB"));
                party = "Democrat";
                l.setBackgroundColor(Color.parseColor("#2D9CDB"));
                text1.setTextColor(Color.parseColor("#2D9CDB"));
                text2.setTextColor(Color.parseColor("#2D9CDB"));
                text3.setTextColor(Color.parseColor("#2D9CDB"));
            } else if (person.getString("party").equals("R")){
                party = "Republican";
                img.setBorderColor(Color.parseColor("#EB5757"));
                l.setBackgroundColor(Color.parseColor("#EB5757"));
                text1.setTextColor(Color.parseColor("#EB5757"));
                text2.setTextColor(Color.parseColor("#EB5757"));
                text3.setTextColor(Color.parseColor("#EB5757"));
            } else if (person.getString("party").equals("I")) {
                party = "Independent";
            }
            if (person.getString("title").equals("Rep")){
                title = "Representative";
            } else if (person.getString("title").equals("Sen")){
                title = "Senator";
            }
            TextView name = (TextView) findViewById(R.id.name);
            name.setText(title + " " + person.getString("first_name") + " " + person.getString("last_name"));
            TextView partyText = (TextView) findViewById(R.id.party);
            partyText.setText(party);
            TextView term = (TextView) findViewById(R.id.term);
            term.setText(dateFormatter(person.getString("term_end")) + "\n");
            TextView committees = (TextView) findViewById(R.id.committees);
            String comString = "";
            for (int i = 0; i < coms.length(); i++) {
                JSONObject com = coms.getJSONObject(i);
                comString += com.getString("name") + "\n";
            }
            committees.setText(comString);
            TextView bills = (TextView) findViewById(R.id.bills);
            String billString = "";
            int numBills = 0;
            for (int i = 0; i < billsArray.length() && numBills != 4; i++) {
                JSONObject bill = billsArray.getJSONObject(i);
                if (bill.getString("short_title").equals("null")){
                    continue;
                }
                billString += bill.getString("short_title") + "\nIntroduced on: " + dateFormatter(bill.getString("introduced_on")) + "\n\n";
                numBills++;
            }
            bills.setText(billString);
        } catch (JSONException e){
            Log.d("T", "in Detailed, gotttt: " + e.getMessage());
        }
    }

    public String dateFormatter(String date){
        String[] dateArray = date.split("-");
        String result = dateArray[1]+"/"+dateArray[2]+"/"+dateArray[0];
        return result;
    }

    private class MyTask extends AsyncTask<String,Void,String>{
        String output;

        public MyTask(String output) {
            this.output = output;
        }
        @Override
        protected String doInBackground(String... params){
            String url = params[0];
            try {
                Log.d("T", "in task, got error: " + new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next());
                if (url.contains("legislators")){
                    DetailedActivity.out = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
                } else if (url.contains("committees")){
                    DetailedActivity.out2 = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
                } else if (url.contains("bills")){
                    DetailedActivity.out3 = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
                }
                return new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();

            } catch (Exception e){
                Log.d("T", "in Congress3, got error: " + e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String result) {
            output = result;
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