package com.appacitive.core.interfaces;

import com.appacitive.core.AppacitiveUser;

/**
 * Created by sathley.
 */
public interface UserContextProvider {

    public String getCurrentlyLoggedInUserToken();

    public void setCurrentlyLoggedInUserToken(String userToken);

    public AppacitiveUser getLoggedInUser();

    public void setLoggedInUser(AppacitiveUser loggedInUser);

    public void setCurrentLocation(Double latitude, Double longitude);

    public double[] getCurrentLocation();

}
