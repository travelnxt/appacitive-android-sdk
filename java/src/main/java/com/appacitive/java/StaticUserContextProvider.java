package com.appacitive.java;

import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.interfaces.UserContextProvider;

/**
 * Created by sathley.
 */
public class StaticUserContextProvider implements UserContextProvider {

    private static String userToken;

    private static AppacitiveUser loggedInUser;

    private static Double[] currentGeoCoordinates = new Double[2];
    @Override
    public String getCurrentlyLoggedInUserToken() {
        return StaticUserContextProvider.userToken;
    }

    @Override
    public void setCurrentlyLoggedInUserToken(String userToken) {
        StaticUserContextProvider.userToken = userToken;
    }

    @Override
    public AppacitiveUser getLoggedInUser() {
        return StaticUserContextProvider.loggedInUser;
    }

    @Override
    public void setLoggedInUser(AppacitiveUser loggedInUser) {
        StaticUserContextProvider.loggedInUser = loggedInUser;
    }

    @Override
    public void setCurrentLocation(Double latitude, Double longitude) {
        StaticUserContextProvider.currentGeoCoordinates[0] = latitude;
        StaticUserContextProvider.currentGeoCoordinates[1] = longitude;
    }

    @Override
    public Double[] getCurrentLocation() {
        return currentGeoCoordinates;
    }
}
