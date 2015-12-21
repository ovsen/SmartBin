package com.danielstone.smartbinapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FetchDataActivity extends AppCompatActivity {

    final String LOGTAG = "FetchDataActivity";
    CoordinatorLayout coordinatorLayout;

    public class QueryFetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //query1
            int fillThreshold = 70;

            final ArrayList<ParseObject> finalObjects = new ArrayList<ParseObject>();

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AllBins");
            query.whereGreaterThan("FillAmount", fillThreshold);

            backgroundTry: try {
                List<ParseObject> objects = query.find();

                if (objects.size() > 0) {

                    for (ParseObject object : objects) {
                        //Log.i(LOGTAG, "Object: " + Integer.toString(object.getInt("FillAmount")));
                        finalObjects.add(object);
                    }

                }

                //Log.i(LOGTAG, "Size " + finalObjects.size());
                ArrayList<Integer> fullBinIDs = new ArrayList<>();
                int running = 0;
                for (ParseObject object : finalObjects) {
                    boolean found = false;
                    for(int id : fullBinIDs) {
                        if (id == object.getInt("binID")) {

                            found = true;
                        }
                    }
                    if (found) {
                        finalObjects.remove(running);
                    } else {
                        fullBinIDs.add(object.getInt("binID"));
                    }
                    running ++;
                }
                //hfh
                for (ParseObject object : finalObjects) {
                    int currentID = object.getInt("binID");
                    //Log.i(LOGTAG, "Bins that need emptying: " + String.valueOf(currentID));
                }

                int finalObjectsSize = finalObjects.size();


                fullBinIDs.clear();
                String[] lngArray = new String[finalObjectsSize];
                String[] latArray = new String[finalObjectsSize];
                running = 0;
                for (ParseObject finalObjectCheck : finalObjects) {

                    fullBinIDs.add(finalObjectCheck.getInt("binID"));

                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("BinLocation");

                    query2.whereEqualTo("binID", finalObjectCheck.getInt("binID"));

                    List<ParseObject> lnglatQueryObjects = query2.find();

                    if (lnglatQueryObjects.size() == 1) {

                        for (ParseObject lnglatObject : lnglatQueryObjects) {
                            lngArray[running] = lnglatObject.getString("Lng");
                            latArray[running] = lnglatObject.getString("Lat");
                            //Log.i(LOGTAG, lngArray[running]);
                            //Log.i(LOGTAG, latArray[running]);
                        }

                    } else {
                        break backgroundTry;
                    }

                    running ++;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*
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
            */
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


        QueryFetchData queryFetchData = new QueryFetchData();
        queryFetchData.execute();

        //query2




    }

}
