package com.appacitive.sdk;

/**
 * Created by sathley.
 */
public class ConnectedObjectsResponse extends PagedList<ConnectedObject> {

    public String parent = null;

    public ConnectedObjectsResponse(String parent)
    {
        this.parent = parent;
    }

}
