package org.roboriotteam3418.frc3418scouting;

import android.app.Application;
import android.content.Context;

/**
 * Created by cstark on 1/5/2017.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
