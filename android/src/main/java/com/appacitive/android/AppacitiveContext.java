package com.appacitive.android;

import android.content.Context;
import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.model.Environment;

/**
 * Created by sathley.
 */
public class AppacitiveContext extends AppacitiveContextBase {

    private static Context applicationContext = null;

    public synchronized static void initialize(String apiKey, Environment environment, Context applicationContext) {
        AppacitiveContextBase.initialize(apiKey, environment, new AndroidPlatform());
        AppacitiveContext.applicationContext = applicationContext;
    }

    public synchronized static Context getApplicationContext() {
        return AppacitiveContext.applicationContext;
    }

}