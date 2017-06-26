package com.feng.retrofit;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chexiangjia-MAC on 17/6/26.
 */

public class MApplication extends Application {
    private static Context sContext = null;
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
