package com.ding.commonlibrary;

import android.app.Application;
import android.content.Context;

/**
 * Created by ding on 18-4-24.
 */

public class MainApp extends Application {
    private static MainApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static Context getInstance() {
        return mApp;
    }
}
