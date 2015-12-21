package com.danielstone.smartbinapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FetchDataActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOGTAG = "FetchDataActivity";
    CoordinatorLayout coordinatorLayout;
    RecyclerView rv;
    FloatingActionButton fab;
    boolean dataDone = false;
    static ArrayList<Integer> fullBinIDs;
    static ArrayList<String> lngArray;
    static ArrayList<String> latArray;
    private List<BinInfo> binInfoList;
    RVAdapter rvAdapter;


    class BinInfo {
        String LNG;
        String LAT;
        String ID;

        BinInfo(String LNG, String LAT, String ID) {
            this.LNG = LNG;
            this.LAT = LAT;
            this.ID = ID;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                if (dataDone) {

                    Log.i("Arrays", String.valueOf(lngArray.size()) + " " + String.valueOf(latArray.size()) + " " + String.valueOf(fullBinIDs.size()));

                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Snackbar.make(coordinatorLayout, "Please wait...", Snackbar.LENGTH_SHORT);
                }
        }
    }

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
                fullBinIDs = new ArrayList<>();
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
                lngArray = new ArrayList<String>();
                latArray = new ArrayList<String>();
                binInfoList.clear();
                running = 0;
                for (ParseObject finalObjectCheck : finalObjects) {

                    fullBinIDs.add(finalObjectCheck.getInt("binID"));

                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("BinLocation");

                    query2.whereEqualTo("binID", finalObjectCheck.getInt("binID"));

                    List<ParseObject> lnglatQueryObjects = query2.find();

                    if (lnglatQueryObjects.size() == 1) {

                        for (ParseObject lnglatObject : lnglatQueryObjects) {
                            lngArray.add(running, lnglatObject.getString("Lng"));
                            latArray.add(running, lnglatObject.getString("Lat"));
                            //Log.i(LOGTAG, lngArray[running]);
                            //Log.i(LOGTAG, latArray[running]);

                            binInfoList.add(new BinInfo(lnglatObject.getString("Lng"), lnglatObject.getString("Lat"), "Bin " + finalObjectCheck.getInt("binID")));
                        }

                    } else {
                        break backgroundTry;
                    }

                    running ++;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            fab.animate().translationYBy(-400).setDuration(500);
            dataDone = true;

            rvAdapter.notifyDataSetChanged();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView)findViewById(R.id.rv);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_fetch_data_coordinator);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.animate().translationYBy(400).setDuration(0);

        LinearLayoutManager llm = new LinearLayoutManager(FetchDataActivity.this);
        rv.setLayoutManager(llm);

        binInfoList = new ArrayList<>();
        binInfoList.add(new BinInfo("loading..", "loading..", "..."));

        rvAdapter = new RVAdapter(binInfoList);
        rv.setAdapter(rvAdapter);

        QueryFetchData queryFetchData = new QueryFetchData();
        queryFetchData.execute();


        //query2

    }

}
