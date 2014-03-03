package com.appacitive.sdk;

import com.appacitive.sdk.infra.APContainer;
import com.appacitive.sdk.infra.JavaPlatform;
import com.appacitive.sdk.model.Environment;
import com.appacitive.sdk.model.Platform;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class AppacitiveContext implements Serializable {

    private volatile static Double[] currentLocation = new Double[2];
    private static String loggedInUserToken;
    private static String apiKey;
    private static String environment;
    private static boolean isInitialized = false;

    public static synchronized String getLoggedInUserToken() {
        return loggedInUserToken;
    }

    public static synchronized void setLoggedInUserToken(String userToken) {
        AppacitiveContext.loggedInUserToken = userToken;
    }

    public static synchronized void initialize(String apiKey, Environment environment) {
        AppacitiveContext.apiKey = apiKey;
        AppacitiveContext.environment = environment.name();
        APContainer.registerAll(new JavaPlatform().getRegistrations());
        ExecutorServiceWrapper.init();
        isInitialized = true;
    }

    public static synchronized void initialize(String apiKey, Environment environment, Platform platform){
        AppacitiveContext.apiKey = apiKey;
        AppacitiveContext.environment = environment.name();
        if(platform == null)
            throw new IllegalArgumentException("Please specify platform or use the overload which does not need it.");
        APContainer.registerAll(platform.getRegistrations());
        ExecutorServiceWrapper.init();
        isInitialized = true;
    }

    public static synchronized void logout() {
        setLoggedInUserToken(null);
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static synchronized void shutdown() {
        ExecutorServiceWrapper.shutdown();
    }

    public static synchronized Double[] getCurrentLocation() {
        return currentLocation;
    }

    public static synchronized void setCurrentLocation(Double latitude, Double longitude) {
        AppacitiveContext.currentLocation[0] = latitude;
        AppacitiveContext.currentLocation[1] = longitude;
    }

    public static synchronized String getApiKey() {
        return apiKey;
    }

    public static synchronized String getEnvironment() {
        return environment;
    }
}
