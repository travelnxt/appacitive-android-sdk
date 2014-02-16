package com.appacitive.sdk;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class AppacitiveContext implements Serializable {

    private static String loggedInUserToken;

    public static String apiKey;

    public static String environment;

    private static boolean isInitialized = false;

    public static final  String getLoggedInUserToken() {
        return loggedInUserToken;
    }

    public static final void setLoggedInUserToken(String userToken) {
        AppacitiveContext.loggedInUserToken = userToken;
    }

    public static final void initialize(String apiKey, Environment environment)
    {
        AppacitiveContext.apiKey = apiKey;
        AppacitiveContext.environment = environment.name();
        ExecutorServiceWrapper.init();
        isInitialized = true;
    }

    public static final boolean isInitialized()
    {
        return isInitialized;
    }

    public static void shutdown()
    {
        ExecutorServiceWrapper.shutdown();
    }

    private static final Double[] currentLocation = new Double[2];

    public static final Double[] getCurrentLocation() {
        return currentLocation;
    }

    public static final void setCurrentLocation(Double latitude, Double longitude) {
        AppacitiveContext.currentLocation[0] = latitude;
        AppacitiveContext.currentLocation[1] = longitude;
    }
}
