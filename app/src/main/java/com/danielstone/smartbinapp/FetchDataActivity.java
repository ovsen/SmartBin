package com.danielstone.smartbinapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FetchDataActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    final String LOGTAG = "FetchDataActivity";
    CoordinatorLayout coordinatorLayout;
    ListView listView;

    String[] lngArray;
    String[] latArray;
    ArrayList<Integer> fullBinIDs;

    /*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(FetchDataActivity.this,lngArray,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    */

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
                lngArray = new String[finalObjectsSize];
                latArray = new String[finalObjectsSize];
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
        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.bin_list);

        final TextView infoTextView = (TextView) findViewById(R.id.infoTextView);


        QueryFetchData queryFetchData = new QueryFetchData();
        queryFetchData.execute();

        //query2




    }

}
