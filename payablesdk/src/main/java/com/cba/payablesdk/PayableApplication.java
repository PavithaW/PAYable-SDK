package com.cba.payablesdk;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Dell on 12/5/2016.
 */

public class PayableApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
