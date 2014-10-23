package com.appacitive.core.model.containers;

import com.appacitive.core.model.AppacitiveObjectBase;

import java.util.UUID;

/**
* Created by sathley on 10/22/2014.
*/
public class ObjectContainer
{
    public AppacitiveObjectBase object = null;
    public String name;
    public long revision = 0;
    public ObjectContainer()
    {
        name = UUID.randomUUID().toString().replaceAll("-", "");
    }
}
