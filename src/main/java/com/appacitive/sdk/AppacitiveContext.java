package com.appacitive.sdk;

import com.appacitive.sdk.infra.Environment;

/**
 * Created by sathley.
 */
public class AppacitiveContext {

    private static String loggedInUserToken;

    public static String apiKey;

    public static String environment;

    private static boolean isInitialized = false;

    public static String getLoggedInUserToken() {
        return loggedInUserToken;
    }

    public static void setLoggedInUserToken(String userToken) {
        AppacitiveContext.loggedInUserToken = userToken;
    }

    public static void initialize(String apiKey, Environment environment)
    {
        AppacitiveContext.apiKey = apiKey;
        AppacitiveContext.environment = environment.name();
        ExecutorServiceWrapper.init();
        isInitialized = true;
    }

    public static boolean isInitialized()
    {
        return isInitialized;
    }

    public static void shutdown()
    {
        ExecutorServiceWrapper.shutdown();
    }

    private static Double[] currentLocation = new Double[2];

    public static Double[] getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(Double latitude, Double longitude) {
        AppacitiveContext.currentLocation[0] = latitude;
        AppacitiveContext.currentLocation[1] = longitude;
    }
}
