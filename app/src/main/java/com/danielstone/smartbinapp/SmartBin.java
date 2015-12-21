package com.danielstone.smartbinapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class SmartBin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this);

        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
