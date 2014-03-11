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

    public static void setLogger(Logger logger) {
        AppacitiveContextBase.logger = logger;
    }

    public static String getLoggedInUserToken() {
        return userContextProvider.getCurrentlyLoggedInUserToken();
    }

    public static AppacitiveUser getLoggedInUser() {
        return userContextProvider.getLoggedInUser();
    }


    public static void setLoggedInUserToken(String userToken) {
        userContextProvider.setCurrentlyLoggedInUserToken(userToken);
    }

    public static void setLoggedInUser(AppacitiveUser user) {
        userContextProvider.setLoggedInUser(user);
    }

    public static void initialize(String apiKey, Environment environment, Platform platform) {
        AppacitiveContextBase.apiKey = apiKey;
        AppacitiveContextBase.environment = environment.name();
        if (platform != null)
            APContainer.registerAll(platform.getRegistrations());


        AppacitiveContextBase.logger = APContainer.build(Logger.class);
        AppacitiveContextBase.userContextProvider = APContainer.build(UserContextProvider.class);

        isInitialized = true;
    }

    public static void register(Class<?> interfaceObject, ObjectFactory<?> objectFactory)
    {
        APContainer.register(interfaceObject, objectFactory);
    }

    public static void logout() {
        setLoggedInUserToken(null);
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static Double[] getCurrentLocation() {
        return userContextProvider.getCurrentLocation();
    }

    public static void setCurrentLocation(Double latitude, Double longitude) {
        userContextProvider.setCurrentLocation(latitude, longitude);
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getEnvironment() {
        return environment;
    }

    public static void setLogLevel(LogLevel logLevel) {
        if (AppacitiveContextBase.logger != null) {
            AppacitiveContextBase.logger.setLogLevel(logLevel);
        }
    }
}
