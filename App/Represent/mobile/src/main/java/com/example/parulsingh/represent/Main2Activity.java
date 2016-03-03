package com.example.parulsingh.represent;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        if (intent.hasExtra("key2") && intent.getStringExtra("key2").equals("95070")){
            updateContent();
        } else if (intent.hasExtra("key3") && intent.getStringExtra("key3").equals("shake")){
            updateContent2();
        }
        Button buttonEshoo = (Button) findViewById(R.id.buttonEshoo);
        buttonEshoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main2Activity.this, EshooActivity.class);
                Main2Activity.this.startActivity(myIntent);
            }
        });
        Button buttonBoxer = (Button) findViewById(R.id.buttonBoxer);
        buttonBoxer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main2Activity.this, BoxerActivity.class);
                Main2Activity.this.startActivity(myIntent);
            }
        });
        Button buttonHonda = (Button) findViewById(R.id.buttonHonda);
        buttonHonda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Main2Activity.this, HondaActivity.class);
                Main2Activity.this.startActivity(myIntent);
            }
        });
        Button latestTweet1 = (Button) findViewById(R.id.latestTweet1);
        latestTweet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getBaseContext(), "Vote for me in 2017!", duration);
                toast.show();
            }
        });
        Button latestTweet2 = (Button) findViewById(R.id.latestTweet2);
        latestTweet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getBaseContext(), "I support gay marriage!", duration);
                toast.show();
            }
        });
        Button latestTweet3 = (Button) findViewById(R.id.latestTweet3);
        latestTweet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getBaseContext(), "Who is the next Supreme Court justice?", duration);
                toast.show();
            }
        });
    }
    void updateContent(){
        ImageView img1 = (ImageView)findViewById(R.id.image1);
        ImageView img2 = (ImageView)findViewById(R.id.image2);
        ImageView img3 = (ImageView)findViewById(R.id.image3);
        Drawable myIcon = getResources().getDrawable(R.mipmap.eshoo);
        img1.setImageDrawable(myIcon);
        img2.setImageDrawable(myIcon);
        img3.setImageDrawable(myIcon);
        TextView text1 = (TextView)findViewById(R.id.info_text);
        TextView text2 = (TextView)findViewById(R.id.info_text2);
        TextView text3 = (TextView)findViewById(R.id.info_text3);
        text1.setText("Dianne Feinstein\nDemocrat\nhttps://feinstein.senate.gov/\nfeinstein@senate.gov\n'Work hard. Dream big.'");
        text2.setText("Dianne Feinstein\nDemocrat\nhttps://feinstein.senate.gov/\nfeinstein@senate.gov\n'Work hard. Dream big.'");
        text3.setText("Dianne Feinstein\nDemocrat\nhttps://feinstein.senate.gov/\nfeinstein@senate.gov\n'Work hard. Dream big.'");
    }
    void updateContent2(){
        ImageView img1 = (ImageView)findViewById(R.id.image1);
        ImageView img2 = (ImageView)findViewById(R.id.image2);
        ImageView img3 = (ImageView)findViewById(R.id.image3);
        Drawable myIcon = getResources().getDrawable(R.mipmap.rodgers);
        img1.setImageDrawable(myIcon);
        myIcon = getResources().getDrawable(R.mipmap.shelby);
        img2.setImageDrawable(myIcon);
        myIcon = getResources().getDrawable(R.mipmap.sessions);
        img3.setImageDrawable(myIcon);
        TextView text1 = (TextView)findViewById(R.id.info_text);
        TextView text2 = (TextView)findViewById(R.id.info_text2);
        TextView text3 = (TextView)findViewById(R.id.info_text3);
        text1.setText("Congressman Mike Rodgers\nRepublican\nhttps://honda.house.gov/\nhonda@house.gov\n“To be great is to be alive”");
        text2.setText("Senator Richard Shelby\nRepublican\nhttps://honda.house.gov/\nhonda@house.gov\n“To be great is to be alive”");
        text3.setText("Senator Jefferson Sessions\nRepublican\nhttps://honda.house.gov/\nhonda@house.gov\n“To be great is to be alive”");
    }
}
