package com.appacitive.java;


import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.model.Environment;

/**
 * Created by sathley.
 */
public class AppacitiveContext extends AppacitiveContextBase {

    public static synchronized void initialize(String apiKey, Environment environment) {
        AppacitiveContextBase.initialize(apiKey, environment, new JavaPlatform());

    }

}
