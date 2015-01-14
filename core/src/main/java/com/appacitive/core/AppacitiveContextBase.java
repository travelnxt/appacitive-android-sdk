package com.appacitive.core;

import com.appacitive.core.infra.APContainer;
import com.appacitive.core.infra.ObjectFactory;
import com.appacitive.core.interfaces.LogLevel;
import com.appacitive.core.interfaces.Logger;
import com.appacitive.core.interfaces.UserContextProvider;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.Platform;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class AppacitiveContextBase implements Serializable {

    private static String apiKey;
    private static String environment;
    private static boolean isInitialized = false;
    private static UserContextProvider userContextProvider = null;
    private static Logger logger;
    public static String baseUrl = "https://apis.appacitive.com/v1.0";

    public synchronized static void setBaseUrl(String url)
    {
        AppacitiveContextBase.baseUrl = url;
    }

    public synchronized static void setLogger(Logger logger) {
        AppacitiveContextBase.logger = logger;
    }

    public synchronized static String getLoggedInUserToken() {
        return userContextProvider.getCurrentlyLoggedInUserToken();
    }

    public synchronized static AppacitiveUser getLoggedInUser() {
        return userContextProvider.getLoggedInUser();
    }


    public synchronized static void setLoggedInUserToken(String userToken) {
        userContextProvider.setCurrentlyLoggedInUserToken(userToken);
    }

    public synchronized static void setLoggedInUser(AppacitiveUser user) {
        userContextProvider.setLoggedInUser(user);
    }

    public synchronized static void initialize(String apiKey, Environment environment, Platform platform) {
        AppacitiveContextBase.apiKey = apiKey;
        AppacitiveContextBase.environment = environment.name();
        if (platform != null)
            APContainer.registerAll(platform.getRegistrations());


        AppacitiveContextBase.logger = APContainer.build(Logger.class);
        AppacitiveContextBase.userContextProvider = APContainer.build(UserContextProvider.class);

        isInitialized = true;
    }

    public synchronized static void register(Class<?> interfaceObject, ObjectFactory<?> objectFactory) {
        APContainer.register(interfaceObject, objectFactory);
    }

    public static void logout() {
        setLoggedInUserToken(null);
    }

    public synchronized static boolean isInitialized() {
        return isInitialized;
    }

    public synchronized static double[] getCurrentLocation() {
        return userContextProvider.getCurrentLocation();
    }

    public synchronized static void setCurrentLocation(Double latitude, Double longitude) {
        userContextProvider.setCurrentLocation(latitude, longitude);
    }

    public synchronized static String getApiKey() {
        return apiKey;
    }

    public synchronized static String getEnvironment() {
        return environment;
    }

    public synchronized static void setLogLevel(LogLevel logLevel) {
        if (AppacitiveContextBase.logger != null) {
            AppacitiveContextBase.logger.setLogLevel(logLevel);
        }
    }
}
