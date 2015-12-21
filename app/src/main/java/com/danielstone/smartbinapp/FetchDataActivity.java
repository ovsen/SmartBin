package com.danielstone.smartbinapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class FetchDataActivity extends AppCompatActivity {

    final String LOGTAG = "FetchDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView infoTextView = (TextView) findViewById(R.id.infoTextView);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BinLocation");
        Log.i(LOGTAG, "1");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.i(LOGTAG, "2");
                    Log.i(LOGTAG, Integer.toString(objects.size()));
                    if (objects.size() > 0) {
                        Log.i(LOGTAG, "3");
                        String result = "";
                        for (ParseObject object : objects) {
                            String currentLat = object.getString("Lat");
                            String currentLong = object.getString("Long");
                            result = result + "Bin " + String.valueOf(object.getInt("binID")) + ": " + currentLat + ", " + currentLong + "\n";
                            Log.i(LOGTAG, currentLat + ", " + currentLong);
                        }
                        infoTextView.setText(result);
                    }
                }
            }
        });
    }

}
