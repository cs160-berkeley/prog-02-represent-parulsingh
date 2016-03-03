package com.example.parulsingh.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                myIntent.putExtra("key", "Saratoga,CA"); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("NAME", "11111");
                startService(sendIntent);
            }
        });

        final EditText zipcode = (EditText)findViewById(R.id.editText);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                myIntent.putExtra("key2", zipcode.getText().toString()); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("NAME", zipcode.getText().toString());
                startService(sendIntent);
            }
        });

    }
}
