package com.danielstone.smartbinapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FetchDataActivity extends AppCompatActivity {

    final String LOGTAG = "FetchDataActivity";
    CoordinatorLayout coordinatorLayout;

    public class queryFetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //query1
            int fillThreshold = 70;

            final ArrayList<ParseObject> fullBinObjects = new ArrayList<ParseObject>();

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AllBins");
            query.whereGreaterThan("FillAmount", fillThreshold);

            try {
                List<ParseObject> objects = query.find();
                if (objects.size() > 0) {
                    for (ParseObject object : objects) {
                        Log.i(LOGTAG, "Object: " + Integer.toString(object.getInt("FillAmount")));
                        fullBinObjects.add(object);
                    }


                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("BinLocation");
            for (ParseObject object : fullBinObjects) {
                Log.i(LOGTAG, "Bins that need emptying: " + Integer.toString(object.getInt("BinId")));
            }


            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //Log.i(LOGTAG, "2");
                        //Log.i(LOGTAG, Integer.toString(objects.size()));
                        if (objects.size() > 0) {
                            //Log.i(LOGTAG, "3");
                            String result = "";
                            for (ParseObject object : objects) {
                                String currentLat = object.getString("Lat");
                                String currentLong = object.getString("Long");
                                result = result + "Bin " + String.valueOf(object.getInt("binID")) + ": " + currentLat + ", " + currentLong + "\n";
                                //Log.i(LOGTAG, currentLat + ", " + currentLong);
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_fetch_data_coordinator);

        final TextView infoTextView = (TextView) findViewById(R.id.infoTextView);




        //query2




    }

}
