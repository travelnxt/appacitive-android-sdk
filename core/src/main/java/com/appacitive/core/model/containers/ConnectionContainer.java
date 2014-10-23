package com.appacitive.core.model.containers;

import com.appacitive.core.AppacitiveConnection;

import java.util.UUID;

/**
* Created by sathley on 10/22/2014.
*/
public class ConnectionContainer
{
    public AppacitiveConnection connection = null;
    public String name;
    public long revision = 0;
    String objectNameA;
    String objectNameB;
    public ConnectionContainer()
    {
        name = UUID.randomUUID().toString().replaceAll("-", "");
    }
}
