package org.roboriotteam3418.frc3418scouting;

import android.app.Application;
import android.content.Context;

/**
 * Created by cstark on 1/5/2017.
 */

public class MyApplication extends Application {

    private Context context;

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private MyApplication singleton;

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public MyApplication getApp() {
        if(singleton == null) {
            singleton = new MyApplication();
        }

        return singleton;
    }

    public Context getAppContext() {
        return this.context;
    }

    public void requestPermission(int permission) {
        switch (permission) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // TODO
                break;
            default:
                // TODO
                break;
        }
    }
}
