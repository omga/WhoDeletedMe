package com.myapps.whodeletedme;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * Created by user on 28.05.15.
 */

public class Application extends android.app.Application {
    // Debugging switch
    public static final boolean APPDEBUG = false;
    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseObject.registerSubclass(FBFriend.class);
        ParseObject.registerSubclass(User.class);

        FacebookSdk.sdkInitialize(getApplicationContext());

        ParseFacebookUtils.initialize(this);
        preferences = getSharedPreferences("com.woobledev.wooble", Context.MODE_PRIVATE);

    }


}