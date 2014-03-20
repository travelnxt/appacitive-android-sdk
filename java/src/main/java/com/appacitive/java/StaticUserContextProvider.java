package com.appacitive.java;

import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.interfaces.UserContextProvider;

/**
 * Created by sathley.
 */
public class StaticUserContextProvider implements UserContextProvider {

    private static String userToken;

    private static AppacitiveUser loggedInUser;

    private static double[] currentGeoCoordinates = new double[2];
    @Override
    public synchronized String getCurrentlyLoggedInUserToken() {
        return StaticUserContextProvider.userToken;
    }

    @Override
    public synchronized void setCurrentlyLoggedInUserToken(String userToken) {
        StaticUserContextProvider.userToken = userToken;
    }

    @Override
    public synchronized AppacitiveUser getLoggedInUser() {
        return StaticUserContextProvider.loggedInUser;
    }

    @Override
    public synchronized void setLoggedInUser(AppacitiveUser loggedInUser) {
        StaticUserContextProvider.loggedInUser = loggedInUser;
    }

    @Override
    public synchronized void setCurrentLocation(Double latitude, Double longitude) {
        StaticUserContextProvider.currentGeoCoordinates[0] = latitude;
        StaticUserContextProvider.currentGeoCoordinates[1] = longitude;
    }

    @Override
    public synchronized double[] getCurrentLocation() {
        return currentGeoCoordinates;
    }
}
