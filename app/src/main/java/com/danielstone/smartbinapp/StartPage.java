package com.danielstone.smartbinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class StartPage extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;

    String LOGTAG = "StartPageActivity";

    public void getRoute(View view) {

        boolean isConnected = (new MyContextWrapper(StartPage.this)).isNetworkConnected();
        if (isConnected) {
            Intent i = new Intent(getApplicationContext(), FetchDataActivity.class);
            startActivity(i);
            finish();
        } else {
            Snackbar.make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_SHORT).show();
            Log.i(LOGTAG, "No internet");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_start_page_coordinator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
