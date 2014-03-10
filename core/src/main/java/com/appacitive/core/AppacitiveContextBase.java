package com.appacitive.core;

import com.appacitive.core.infra.APContainer;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.Platform;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public abstract class AppacitiveContextBase implements Serializable {

    private volatile static Double[] currentLocation = new Double[2];
    private static String loggedInUserToken;
    private static String apiKey;
    private static String environment;
    private static boolean isInitialized = false;

    public static String getLoggedInUserToken() {
        return loggedInUserToken;
    }

    public static synchronized void setLoggedInUserToken(String userToken) {
        AppacitiveContextBase.loggedInUserToken = userToken;
    }

    public static synchronized void initialize(String apiKey, Environment environment, Platform platform) {
        AppacitiveContextBase.apiKey = apiKey;
        AppacitiveContextBase.environment = environment.name();
        if (platform == null)
            throw new IllegalArgumentException("Please specify platform.");
        APContainer.registerAll(platform.getRegistrations());
        isInitialized = true;
    }

    public static synchronized void logout() {
        setLoggedInUserToken(null);
    }

    public static synchronized boolean isInitialized() {
        return isInitialized;
    }

    public static Double[] getCurrentLocation() {
        return currentLocation;
    }

    public static synchronized void setCurrentLocation(Double latitude, Double longitude) {
        AppacitiveContextBase.currentLocation[0] = latitude;
        AppacitiveContextBase.currentLocation[1] = longitude;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getEnvironment() {
        return environment;
    }
}
