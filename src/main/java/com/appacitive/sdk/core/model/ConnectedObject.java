package com.appacitive.sdk.core.model;

import com.appacitive.sdk.core.AppacitiveConnection;
import com.appacitive.sdk.core.AppacitiveObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class ConnectedObject implements Serializable {

    public AppacitiveObject object;

    public AppacitiveConnection connection;
}
