package com.appacitive.sdk.model;

import com.appacitive.sdk.AppacitiveConnection;
import com.appacitive.sdk.AppacitiveObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class ConnectedObject implements Serializable {

    public AppacitiveObject object;

    public AppacitiveConnection connection;
}
