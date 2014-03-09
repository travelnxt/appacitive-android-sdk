package com.appacitive.core.model;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class ConnectedObject implements Serializable {

    public AppacitiveObject object;

    public AppacitiveConnection connection;
}
